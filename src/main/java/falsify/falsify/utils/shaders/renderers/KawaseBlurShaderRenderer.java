package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.module.modules.render.BlurModule;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.shaders.Framebuffer;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderManager;
import falsify.falsify.utils.shaders.ShaderRenderer;

import static falsify.falsify.Falsify.mc;

public class KawaseBlurShaderRenderer extends ShaderRenderer {

    private final BlurModule blurModule;
    private Framebuffer[] fbos = new Framebuffer[2];
    private final Shader upScale;
    public KawaseBlurShaderRenderer(BlurModule blurModule) {
        super(new Shader("blur.vert", "kawase_down.frag"));
        this.upScale = new Shader("blur.vert", "kawase_up.frag");
        this.blurModule = blurModule;
        loadFramebuffers();
    }

    public void loadFramebuffers() {
        fbos = new Framebuffer[blurModule.getPasses()];
        float scale = blurModule.getDownscaleFactor();
        for(int i = 0; i < fbos.length; i++) {
            fbos[i] = new Framebuffer((int) (mc.getWindow().getFramebufferWidth() / (scale*(i+1))), (int) (mc.getWindow().getFramebufferHeight() / (scale*(i+1))));
        }
    }

    @Override
    public void loadVariables() {
        shader.set("u_Texture", 0);
        shader.set("u_Radius", blurModule.getCurrentRadius());
        shader.set("u_Scale", blurModule.getDownscaleFactor());
    }

    public void renderShader(int sourceTexture) {
        if(!shouldRender()) return;
        int passes = fbos.length;
        for(int i = 0; i < passes; i++) {
            Framebuffer to;
            int texture;

            if(i == 0) {
                to = fbos[0];
                texture = sourceTexture;
            } else {
                to = fbos[i];
                texture = fbos[i-1].texture;
            }
            kawasePass(texture, to, shader);
        }
        for(int i = passes-2; i >= 0; i--) {
            kawasePass(fbos[i+1].texture, fbos[i], upScale);
        }
        kawasePass(fbos[0].texture, framebuffer, upScale);
        postShaderRender();
    }

    private void kawasePass(int texture, Framebuffer to, Shader shader) {
        ShaderManager.pre(to);
        ShaderManager.bindShader(shader);
        ShaderManager.bindTexture(texture);
        loadVariables();
        shader.set("u_Size", to.width, to.height);
        RenderUtils.renderShader(matrices);
    }

    public void resize() {
        for(Framebuffer fbo : fbos) {
            fbo.resize();
        }
    }

    @Override
    protected boolean shouldRender() {
        return blurModule.isEnabled();
    }
}
