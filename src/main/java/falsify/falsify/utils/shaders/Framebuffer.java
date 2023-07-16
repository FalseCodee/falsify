package falsify.falsify.utils.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import falsify.falsify.Falsify;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL32C.*;

public class Framebuffer {
    private int id;
    public int texture;
    public int width, height;

    public Framebuffer() {
        init();
    }

    private void init() {
        id = GlStateManager.glGenFramebuffers();

        texture = TextureUtil.generateTextureId();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(texture);

        GlStateManager._pixelStore(GL_UNPACK_SWAP_BYTES, GL_FALSE);
        GlStateManager._pixelStore(GL_UNPACK_LSB_FIRST, GL_FALSE);
        GlStateManager._pixelStore(GL_UNPACK_ROW_LENGTH, 0);
        GlStateManager._pixelStore(GL_UNPACK_IMAGE_HEIGHT, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_IMAGES, 0);
        GlStateManager._pixelStore(GL_UNPACK_ALIGNMENT, 4);

        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        width = Falsify.mc.getWindow().getFramebufferWidth();
        height = Falsify.mc.getWindow().getFramebufferHeight();

        GlStateManager._texImage2D(GL_TEXTURE_2D, 0, GL_RGB, Falsify.mc.getWindow().getFramebufferWidth(), Falsify.mc.getWindow().getFramebufferHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, (IntBuffer) null);
        bind();
        GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        unbind();
    }

    public void bind() {
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void setViewport() {
        setViewport(0, 0, width, height);
    }
    public void setViewport(int x1, int y1, int x2, int y2) {
        GlStateManager._viewport(x1, y1, x2, y2);
    }

    public void unbind() {
        Falsify.mc.getFramebuffer().beginWrite(false);
    }

    public void resize() {
        GlStateManager._glDeleteFramebuffers(id);
        GlStateManager._deleteTexture(texture);

        init();
    }
}
