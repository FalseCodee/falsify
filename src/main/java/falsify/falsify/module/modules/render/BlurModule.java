package falsify.falsify.module.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import falsify.falsify.gui.clickgui.primatives.Animation;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.listeners.events.EventWindowResize;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.Timer;
import falsify.falsify.utils.shaders.Framebuffer;
import falsify.falsify.utils.shaders.Shader;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import static org.lwjgl.opengl.GL32C.*;

public class BlurModule extends Module {

    private Shader shader;
    private Shader shadePassthrough;
    private Framebuffer framebuffer;

    private final MatrixStack matrices = new MatrixStack();
    private final RangeSetting radius = new RangeSetting("Radius", 3, 1, 100, 1);

    private final Timer timer = new Timer();

    Animation fadeInOut = new Animation(250, Animation.Type.EASE_IN_OUT);
    public BlurModule() {
        super("Blur", "Blurs the background", false, Category.RENDER, -1);
        settings.add(radius);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender3d e) {
            if(mc.currentScreen == null || mc.currentScreen instanceof ChatScreen) {
                fadeInOut.lower();
            } else {
                fadeInOut.rise();
            }
            fadeInOut.tick();
            if(fadeInOut.getProgress() == 0.0) return;

            blurScreen(fadeInOut.run());

        } else if(event instanceof EventPacketSend e && e.getPacket() instanceof ChatMessageC2SPacket packet && packet.chatMessage().toLowerCase().startsWith(".reload")) {
            shader = null;
        } else if(event instanceof EventWindowResize && framebuffer != null) {
            framebuffer.resize();
        }
    }

    public void blurScreen(double progress) {
        if (shader == null) {
            shader = new Shader("blur.vert", "blur.frag");
            shadePassthrough = new Shader("passthrough.vert", "passthrough.frag");
            framebuffer = new Framebuffer();
        }

        int texture = mc.getFramebuffer().getColorAttachment();
        RenderUtils.renderShaderBegin();
        renderToFbo(framebuffer, texture, shader, progress);
        mc.getFramebuffer().beginWrite(true);
        shadePassthrough.bind();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(framebuffer.texture);
        shadePassthrough.set("uTexture", 0);
        RenderUtils.renderShader(matrices);
        RenderUtils.renderShaderEnd();
    }

    private void renderToFbo(Framebuffer targetFbo, int sourceText, Shader shader, double progress) {
        targetFbo.bind();
        targetFbo.setViewport();
        shader.bind();
        GlStateManager._activeTexture(GL_TEXTURE0);
        GlStateManager._bindTexture(sourceText);
        shader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        shader.set("u_Texture", 0);
        shader.set("u_Radius", radius.getValue()*progress);
        RenderUtils.renderShader(matrices);
    }
}
