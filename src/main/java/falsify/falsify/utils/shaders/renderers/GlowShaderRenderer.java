package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.Falsify;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderRenderer;

import static falsify.falsify.Falsify.mc;

public class GlowShaderRenderer extends ShaderRenderer {
    public GlowShaderRenderer() {
        super(new Shader("blur.vert", "glow.frag"));
    }

    protected GlowShaderRenderer(Shader shader) {
        super(shader);
    }

    @Override
    public void loadVariables() {
        shader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        shader.set("u_Texture", 0);
        shader.set("u_Radius", (Falsify.postProcess != null) ? Falsify.postProcess.getRadius() : 15);
    }

    @Override
    protected boolean shouldRender() {
        return Falsify.postProcess.shouldGlow();
    }
}
