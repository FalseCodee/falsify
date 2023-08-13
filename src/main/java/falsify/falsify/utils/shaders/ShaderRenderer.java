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

    public ShaderRenderer(Shader shader) {
        this.framebuffer = new Framebuffer();
        this.capture = new Framebuffer();
        this.shader = shader;
    }
    public void preShaderRender() {
        ShaderManager.pre(framebuffer);
    }

    public void postShaderRender() {
        ShaderManager.post(framebuffer);
    }

    public abstract void loadVariables();
    protected abstract boolean shouldRender();
    public void renderShader(int sourceTexture) {
        if(Falsify.postProcess == null || !shouldRender()) return;
        preShaderRender();
        ShaderManager.bindShader(shader);
        ShaderManager.bindTexture(sourceTexture);
        loadVariables();
        RenderUtils.renderShader(matrices);
        postShaderRender();
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
        if(Falsify.postProcess == null || !shouldRender()) return;
        if(clear) capture.clear(false);
        capture.bind();
        if(clear) capture.setViewport();
    }

    public Framebuffer getCapture() {
        return capture;
    }
    public void clearCapture() {
        capture.clear(false);
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
}
