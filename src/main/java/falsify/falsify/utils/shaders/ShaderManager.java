package falsify.falsify.utils.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import falsify.falsify.Falsify;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.misc.PostProcess;
import falsify.falsify.module.modules.render.BlurModule;
import falsify.falsify.module.modules.render.Perspective;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.shaders.renderers.DerivativeShaderRenderer;
import falsify.falsify.utils.shaders.renderers.DistortionShaderRenderer;
import falsify.falsify.utils.shaders.renderers.GlowOutlineShaderRenderer;
import falsify.falsify.utils.shaders.renderers.KawaseBlurShaderRenderer;
import net.minecraft.client.util.math.MatrixStack;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;

public class ShaderManager {
    public Shader PASSTHROUGH;
    public Shader MIXTHROUGH;
    private final Framebuffer framebuffer = new Framebuffer();
    public DistortionShaderRenderer DISTORTION;
    public DerivativeShaderRenderer DERIVATIVE_FILTER;
    public GlowOutlineShaderRenderer GLOW_OUTLINE;
    public KawaseBlurShaderRenderer KAWASE_BLUR;
    public final MatrixStack matrices = new MatrixStack();
    public ShaderManager() {
        this.PASSTHROUGH = new Shader("passthrough.vert", "passthrough.frag");
        this.MIXTHROUGH = new Shader("passthrough.vert", "mixthrough.frag");
        this.DISTORTION = new DistortionShaderRenderer(ModuleManager.getModule(Perspective.class));
        this.DERIVATIVE_FILTER = new DerivativeShaderRenderer(ModuleManager.getModule(PostProcess.class));
        this.GLOW_OUTLINE = new GlowOutlineShaderRenderer();
        this.KAWASE_BLUR = new KawaseBlurShaderRenderer(ModuleManager.getModule(BlurModule.class));
        Falsify.logger.info("Created: Shader Manager");
    }

    public void pass(Framebuffer framebuffer) {
        pass(framebuffer.texture);
    }

    public void pass(int framebuffer) {
        Falsify.mc.getFramebuffer().beginWrite(true);
        PASSTHROUGH.bind();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(framebuffer);
        PASSTHROUGH.set("uTexture", 0);
        RenderUtils.renderShader(matrices);
    }

    public void mix(int framebuffer) {
        this.framebuffer.bind();

        MIXTHROUGH.bind();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(Falsify.mc.getFramebuffer().getColorAttachment());
        MIXTHROUGH.set("uTextureBase", 0);
        GlStateManager._activeTexture(GL_TEXTURE1);
        GlStateManager._bindTexture(framebuffer);
        MIXTHROUGH.set("uTextureShader", 1);
        RenderUtils.renderShader(matrices);

        pass(this.framebuffer);
    }

    public static void pre(Framebuffer fbo) {
        RenderUtils.renderShaderBegin();
        fbo.clear(false);
        fbo.bind();
        fbo.setViewport();

    }

    public static void post(Framebuffer fbo, boolean mix) {
        if(mix)
            Falsify.shaderManager.mix(fbo.texture);
        else
            Falsify.shaderManager.pass(fbo.texture);
        RenderUtils.renderShaderEnd();
    }

    public static void bindShader(Shader shader) {
        shader.bind();
    }

    public static void bindTexture(int texture) {
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(texture);
    }

    public static void bindTexture1(int texture) {
        GlStateManager._activeTexture(GL_TEXTURE1);
        GlStateManager._bindTexture(texture);
    }

    public void resize() {
        DISTORTION.getFramebuffer().resize();
        DERIVATIVE_FILTER.getFramebuffer().resize();
        KAWASE_BLUR.resize();
        GLOW_OUTLINE.getFramebuffer().resize();
        GLOW_OUTLINE.getCapture().resize();

        framebuffer.resize();
    }

    public void clear() {
        GLOW_OUTLINE.clearCapture();
        framebuffer.clear(false);
    }

    public void renderWorldShader() {
        DERIVATIVE_FILTER.renderShader(Falsify.mc.getFramebuffer().getColorAttachment());
    }

    public void renderGuiShader() {

    }

    public void renderAllCaptures() {
        GLOW_OUTLINE.renderIfCaptured();
    }
}
