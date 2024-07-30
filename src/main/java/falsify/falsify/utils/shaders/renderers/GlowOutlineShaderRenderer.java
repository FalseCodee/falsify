package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.Falsify;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderRenderer;

public class GlowOutlineShaderRenderer extends ShaderRenderer {
    public GlowOutlineShaderRenderer() {
        super(new Shader("blur.vert", "glowoutline.frag"), true);
    }

    @Override
    public void loadVariables() {
        shader.set("u_Size", Falsify.mc.getWindow().getFramebufferWidth(), Falsify.mc.getWindow().getFramebufferHeight());
        shader.set("u_Texture", 0);
        shader.set("u_Radius", 15.0);
    }

    @Override
    protected boolean shouldRender() {
        return true;
    }
}
