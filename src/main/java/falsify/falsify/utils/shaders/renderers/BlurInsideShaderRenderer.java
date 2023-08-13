package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.Falsify;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderManager;
import falsify.falsify.utils.shaders.ShaderRenderer;

import static falsify.falsify.Falsify.mc;

public class BlurInsideShaderRenderer extends ShaderRenderer {
    public BlurInsideShaderRenderer() {
        super(new Shader("blur.vert", "blurinside.frag"));
    }

    @Override
    public void loadVariables() {
        shader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        shader.set("u_Texture", 0);
        shader.set("u_Inside", 1);
        shader.set("u_Radius", (Falsify.postProcess != null) ? Falsify.postProcess.getRadius() : 15);
    }

    @Override
    protected boolean shouldRender() {
        return Falsify.postProcess.shouldBlurBoxes();
    }

    @Override
    public void renderShader(int sourceTexture) {
        if(Falsify.postProcess == null || !shouldRender()) return;
        preShaderRender();
        ShaderManager.bindShader(shader);
        ShaderManager.bindTexture(mc.getFramebuffer().getColorAttachment());
        ShaderManager.bindTexture1(sourceTexture);
        loadVariables();
        RenderUtils.renderShader(matrices);
        postShaderRender();
    }

    @Override
    public void startCapture(boolean clear) {
        if(Falsify.postProcess == null || !shouldRender()) return;
        if(clear) capture.clear(false);
        capture.bind();
        if(clear) capture.setViewport();
    }
}
