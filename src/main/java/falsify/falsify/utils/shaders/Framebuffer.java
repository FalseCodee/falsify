package falsify.falsify.utils.shaders;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;

import static org.lwjgl.opengl.GL32C.*;

public class Framebuffer {
    private int id;
    public int texture;
    public int depth;
    public int width, height;
    public int texFilter;

    public Framebuffer(int width, int height) {
        init(width, height);
    }
    public Framebuffer() {
        init();
    }

    private void init() {
        init(Falsify.mc.getWindow().getFramebufferWidth(), Falsify.mc.getWindow().getFramebufferHeight());
    }

    private void init(int width, int height) {
        this.width = width;
        this.height = height;

        id = GlStateManager.glGenFramebuffers();

        texture = TextureUtil.generateTextureId();
        this.depth = TextureUtil.generateTextureId();
        GlStateManager._bindTexture(this.depth);

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
        GlStateManager._texImage2D(GlConst.GL_TEXTURE_2D, 0, GlConst.GL_DEPTH_COMPONENT, this.width, this.height, 0, GlConst.GL_DEPTH_COMPONENT, GlConst.GL_FLOAT, null);

        this.setTexFilter(GlConst.GL_NEAREST);
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

        GlStateManager._texImage2D(GlConst.GL_TEXTURE_2D, 0, GlConst.GL_RGBA8, this.width, this.height, 0, GlConst.GL_RGBA, GlConst.GL_UNSIGNED_BYTE, null);
        bind();
        GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
        GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GlConst.GL_DEPTH_ATTACHMENT, GlConst.GL_TEXTURE_2D, this.depth, 0);

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
        Falsify.mc.getFramebuffer().beginWrite(true);
    }

    public void resize() {
        GlStateManager._glDeleteFramebuffers(id);
        GlStateManager._deleteTexture(texture);
        TextureUtil.releaseTextureId(this.depth);

        init();
    }

    public void scale(float scale) {
        GlStateManager._glDeleteFramebuffers(id);
        GlStateManager._deleteTexture(texture);
        TextureUtil.releaseTextureId(this.depth);

        init((int) (width*scale), (int) (height*scale));
    }

    public void clear(boolean getError) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.bind();
        GlStateManager._clearColor(0,0,0,0);
        int i = 16384;
        GlStateManager._clearDepth(1.0);
        i |= 0x100;
        GlStateManager._clear(i, getError);
        this.unbind();
    }

    public void setTexFilter(int texFilter) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.texFilter = texFilter;
        GlStateManager._bindTexture(this.texture);
        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, texFilter);
        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, texFilter);
        GlStateManager._bindTexture(0);
    }



    public void copyDepthFrom(Framebuffer framebuffer) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, framebuffer.id);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, id);
        GlStateManager._glBlitFrameBuffer(0, 0, framebuffer.width, framebuffer.height, 0, 0, this.width, this.height, 256, GlConst.GL_NEAREST);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
    }
    public void copyDepthFrom(net.minecraft.client.gl.Framebuffer framebuffer) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, framebuffer.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, id);
        GlStateManager._glBlitFrameBuffer(0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 0, 0, this.width, this.height, 256, GlConst.GL_NEAREST);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
    }
}
