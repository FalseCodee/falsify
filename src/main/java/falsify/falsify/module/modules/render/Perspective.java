package falsify.falsify.module.modules.render;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;

public class Perspective extends Module {

    private float pitch;
    private float yaw;
    private net.minecraft.client.option.Perspective perspective;

    private double inertia;

    private final BooleanSetting clip = new BooleanSetting("Clip", false);
    private final RangeSetting cameraDistance = new RangeSetting("Distance", 4, 1, 30, 0.25);

    public static Perspective INSTANCE;
    public Perspective() {
        super("Perspective", "Change your perspective without moving your head.", false, Category.RENDER, -1);
        settings.add(clip);
        INSTANCE = this;
    }

    public boolean shouldClip() {
        return clip.getValue();
    }

    public double getCameraDistance() {
        return cameraDistance.getValue();
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public void onEnable() {
        if(mc.player == null) this.toggle();
        pitch = mc.player.getPitch();
        yaw = mc.player.getYaw();
        this.perspective = mc.options.getPerspective();
        mc.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        mc.options.setPerspective(perspective);
        cameraDistance.setValue(4.0);
    }

    @Override
    public void onEvent(Event<?> event) {
        if (event instanceof EventKey e && keybind.getValue() != -1 && e.getKey() == keybind.getValue() && e.getAction() == 0) {
            this.toggle();
        } else if(event instanceof EventMouse.MoveIngame e && e.isPre()) {
            pitch = MathUtils.clamp((float) (pitch + e.getVertical() / 8), -90, 90);
            yaw += e.getHorizontal() / 8;
            e.setCancelled(true);
        } else if(event instanceof EventMouse.Scroll e) {
            inertia += e.getVertical() / 3;
            e.setCancelled(true);
        } else if(event instanceof EventRender e) {
            if(Math.abs(inertia) > 0.1) {
                cameraDistance.setValue(cameraDistance.getValue() - inertia);
                inertia *= 0.75;
            }
        }
    }
}
