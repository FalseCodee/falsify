package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Animation;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.*;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.shaders.Shader;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class BlurModule extends Module {

    private final RangeSetting radius = new RangeSetting("Radius", 3, 0, 100, 1);
    private final RangeSetting passes = new RangeSetting("Passes", 3, 1, 20, 1);
    private final RangeSetting downscale = new RangeSetting("Downsample", 3, 1, 100, 0.01);
    private float currentRadius;
    private final Animation fadeInOut = new Animation(250, Animation.Type.EASE_IN_OUT);
    public BlurModule() {
        super("Blur", "Blurs the background", false, Category.RENDER, -1);
        settings.add(radius);
        settings.add(passes);
        settings.add(downscale);
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

            currentRadius = (float) (radius.getValue() / 10 * (fadeInOut.run()));

            Falsify.shaderManager.KAWASE_BLUR.renderShader(Falsify.mc.getFramebuffer().getColorAttachment());

        } else if(event instanceof EventPacketSend e && e.getPacket() instanceof ChatMessageC2SPacket packet && packet.chatMessage().toLowerCase().startsWith(".reload")) {
            Falsify.shaderManager.GLOW.setShader(new Shader("blur.vert", "glow.frag"));
            Falsify.shaderManager.GLOW_OUTLINE.setShader(new Shader("blur.vert", "glowoutline.frag"));
            Falsify.shaderManager.BLUR_INSIDE.setShader(new Shader("blur.vert", "blurinside.frag"));
            Falsify.shaderManager.KAWASE_BLUR.setShader(new Shader("blur.vert", "kawase_down.frag"));
            event.setCancelled(true);
        } else if(event instanceof EventWindowResize) {
            Falsify.shaderManager.KAWASE_BLUR.loadFramebuffers();
        } else if(event instanceof EventSettingChange<?> e && (e.getSetting() == passes || e.getSetting() == downscale)) {
            Falsify.shaderManager.KAWASE_BLUR.loadFramebuffers();
        }
    }

    public float getCurrentRadius() {
        return currentRadius;
    }

    public int getPasses() {
        return passes.getValue().intValue();
    }

    public float getDownscaleFactor() {
        return downscale.getValue().floatValue();
    }
}
