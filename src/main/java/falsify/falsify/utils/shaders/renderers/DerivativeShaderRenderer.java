package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.module.modules.misc.PostProcess;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderRenderer;

import static falsify.falsify.Falsify.mc;

public class DerivativeShaderRenderer extends ShaderRenderer {
    private final PostProcess postProcess;
    public DerivativeShaderRenderer(PostProcess postProcess) {
        super(new Shader("blur.vert", "derivativefilter.frag"));
        this.postProcess = postProcess;
    }

    @Override
    public void loadVariables() {
        shader.set("u_Texture", 0);
        shader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
    }

    @Override
    protected boolean shouldRender() {
        return postProcess.shouldRenderDerivativeFilter();
    }
}
