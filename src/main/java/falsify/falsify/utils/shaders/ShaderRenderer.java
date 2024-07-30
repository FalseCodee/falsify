package falsify.falsify.utils.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import falsify.falsify.Falsify;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

public abstract class ShaderRenderer {
    protected Framebuffer framebuffer;
    protected final Framebuffer capture;
    protected Shader shader;
    protected final MatrixStack matrices = new MatrixStack();
    protected final boolean mix;
    protected boolean captured = false;

    public ShaderRenderer(Shader shader, boolean mix) {
        this.framebuffer = new Framebuffer();
        this.capture = new Framebuffer();
        this.shader = shader;
        this.mix = mix;
    }

    public ShaderRenderer(Shader shader) {
        this(shader, false);
    }
    public void preShaderRender() {
        ShaderManager.pre(framebuffer);
    }

    public void postShaderRender() {
        ShaderManager.post(framebuffer, mix);
    }

    public abstract void loadVariables();
    protected abstract boolean shouldRender();
    public void renderShader(int sourceTexture) {
        if(!shouldRender()) return;
        preShaderRender();
        ShaderManager.bindShader(shader);
        ShaderManager.bindTexture(sourceTexture);
        loadVariables();
        RenderUtils.renderShader(matrices);
        postShaderRender();
        captured = false;
    }

    public void renderShader() {
        renderShader(capture.texture);
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public void setFramebuffer(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void startCapture(boolean clear) {
        if(!shouldRender()) return;
        if(clear) capture.clear(false);
        capture.bind();
        if(clear) capture.setViewport();
        captured = true;
    }

    public Framebuffer getCapture() {
        return capture;
    }
    public void clearCapture() {
        capture.clear(false);
        captured = false;
//        capture.setViewport();
    }
    public void pass(int framebuffer) {
        startCapture(false);
        Falsify.shaderManager.PASSTHROUGH.bind();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(framebuffer);
        Falsify.shaderManager.PASSTHROUGH.set("uTexture", 0);
        RenderUtils.renderShader(matrices);
    }

    public void endCapture() {
        Falsify.mc.getFramebuffer().beginWrite(true);
    }

    public void renderIfCaptured() {
        if(captured) renderShader();
    }
}
