package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.Falsify;
import falsify.falsify.utils.shaders.Shader;

public class GlowOutlineShaderRenderer extends GlowShaderRenderer {
    public GlowOutlineShaderRenderer() {
        super(new Shader("blur.vert", "glowoutline.frag"));
    }

    @Override
    protected boolean shouldRender() {
        return Falsify.postProcess.shouldGlowOutline();
    }
}
