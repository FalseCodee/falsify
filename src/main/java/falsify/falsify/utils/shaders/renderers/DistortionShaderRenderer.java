package falsify.falsify.utils.shaders.renderers;

import falsify.falsify.module.modules.render.Perspective;
import falsify.falsify.utils.shaders.Shader;
import falsify.falsify.utils.shaders.ShaderRenderer;
import org.lwjgl.glfw.GLFW;

import static falsify.falsify.Falsify.mc;

public class DistortionShaderRenderer extends ShaderRenderer {

    private final Perspective perspective;
    public DistortionShaderRenderer(Perspective perspective) {
        super(new Shader("blur.vert", "distortion.frag"));
        this.perspective = perspective;
    }

    protected DistortionShaderRenderer(Shader shader, Perspective perspective) {
        super(shader);
        this.perspective = perspective;
    }

    @Override
    public void loadVariables() {
        shader.set("u_Texture", 0);
        shader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        shader.set("u_Time", GLFW.glfwGetTime());
    }

    @Override
    protected boolean shouldRender() {
        return perspective.isBomberView();
    }
}
