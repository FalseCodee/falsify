package falsify.falsify.utils.shaders;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import falsify.falsify.Falsify;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.lwjgl.opengl.GL32C.*;

public class Shader {

    private static final FloatBuffer MAT = BufferUtils.createFloatBuffer(4 * 4);
    private final int id;
    private final Object2IntMap<String> uniformLocations = new Object2IntOpenHashMap<>();


    public Shader(String vertPath, String fragPath) {
        int vert = GlStateManager.glCreateShader(GL_VERTEX_SHADER);

        GlStateManager.glShaderSource(vert, ImmutableList.of(read(vertPath)));
        GlStateManager.glCompileShader(vert);

        int frag = GlStateManager.glCreateShader(GL_FRAGMENT_SHADER);
        GlStateManager.glShaderSource(frag, ImmutableList.of(read(fragPath)));
        GlStateManager.glCompileShader(frag);

        this.id = glCreateProgram();

        GlStateManager.glAttachShader(id, vert);
        GlStateManager.glAttachShader(id, frag);
        GlStateManager.glLinkProgram(id);

        if (GlStateManager.glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println(GlStateManager.glGetProgramInfoLog(id, 512));
        }

        GlStateManager.glDeleteShader(vert);
        GlStateManager.glDeleteShader(frag);
    }

    private String read(String filePath){
        try {
            return IOUtils.toString(Objects.requireNonNull(Falsify.class.getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bind() {
        GlStateManager._glUseProgram(id);
    }

    private int getLocation(String name) {
        if (uniformLocations.containsKey(name)) return uniformLocations.getInt(name);

        int location = GlStateManager._glGetUniformLocation(id, name);
        uniformLocations.put(name, location);
        return location;
    }

    public void set(String name, boolean v) {
        glUniform1i(getLocation(name), v ? GL_TRUE : GL_FALSE);
    }

    public void set(String name, int v) {
        glUniform1i(getLocation(name), v);
    }

    public void set(String name, double v) {
        glUniform1f(getLocation(name), (float) v);
    }

    public void set(String name, double v1, double v2) {
        glUniform2f(getLocation(name), (float) v1, (float) v2);
    }

    public void set(String name, Color color) {
        glUniform4f(getLocation(name), (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
    }

    public void set(String name, Matrix4f mat) {
        mat.get(MAT);
        glUniformMatrix4fv(getLocation(name), false, MAT);
    }

    public int getId() {
        return this.id;
    }
}
