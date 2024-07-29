package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.awt.*;

public class Perspective extends Module {

    private float pitch;
    private float yaw;
    private net.minecraft.client.option.Perspective perspective = net.minecraft.client.option.Perspective.FIRST_PERSON;

    private double inertia;

    private final BooleanSetting clip = new BooleanSetting("Clip", false);
    private final BooleanSetting bomberView = new BooleanSetting("Bomber View", false);
    private final BooleanSetting cameraDistortion = new BooleanSetting("Camera Distortion", false);
    private final RangeSetting cameraDistance = new RangeSetting("Distance", 4, 1, 300, 0.25);
    private double bomberDistance = 10;
    public static Perspective INSTANCE;
    public Perspective() {
        super("Perspective", "Change your perspective without moving your head.", false, Category.RENDER, -1);
        settings.add(clip);
        settings.add(bomberView);
        settings.add(cameraDistortion);
        INSTANCE = this;
    }

    public boolean isBomberView() {
        return bomberView.getValue();
    }

    public boolean shouldClip() {
        return clip.getValue();
    }

    public float getCameraDistance() {
        if(!bomberView.getValue()) return cameraDistance.getValue().floatValue();
        BlockPos top = mc.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, mc.player.getBlockPos());
        double newPos = top.getY() - MathUtils.getInterpolatedPos(mc.player, mc.getRenderTickCounter().getLastFrameDuration()).y + 10;
        double lerped = MathUtils.lerp(bomberDistance, newPos, 0.05);
        bomberDistance = lerped;
        return (float) (lerped + cameraDistance.getValue().floatValue()-10);
    }

    public float getPitch() {
        return bomberView.getValue() ? 90 : pitch;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public void onEnable() {
        if(mc.player == null || mc.world == null || mc.options.getPerspective() == null) {
            this.toggle();
            return;
        }
        pitch = mc.player.getPitch();
        yaw = mc.player.getYaw();
        this.perspective = mc.options.getPerspective();
        if(this.perspective == null) this.perspective = net.minecraft.client.option.Perspective.FIRST_PERSON;
        mc.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        if(mc.options.getPerspective() != null) mc.options.setPerspective(perspective);
        cameraDistance.setValue(4.0);
    }

    @Override
    public void onEvent(Event<?> event) {
        if (event instanceof EventKey e && keybind.getValue() != -1 && e.getKey() == keybind.getValue() && e.getAction() == 0 && !bomberView.getValue()) {
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

        if(bomberView.getValue() && event instanceof  EventRender render) {
            int size = (int) (13*(8/(cameraDistance.getValue())));
            float blastRadius = size * 5;

            render.getDrawContext().getMatrices().push();
            render.getDrawContext().getMatrices().translate(mc.getWindow().getScaledWidth()/2f , mc.getWindow().getScaledHeight()/2f, 0);
            Falsify.fontRenderer.drawString(render.getDrawContext(), "Drop time: " + ((int)(dropTime(-bomberDistance+10) * 10) / 10.0f) + "s", blastRadius + 2, -Falsify.fontRenderer.getStringHeight("D")/2, Color.GREEN, true);

            render.getDrawContext().getMatrices().push();
            render.getDrawContext().getMatrices().peek().getPositionMatrix().rotate((float) -Math.toRadians(getYaw() - mc.player.getYaw()), 0, 0, 1);
            render.getDrawContext().drawHorizontalLine(-size, size,-size, Color.green.getRGB());
            render.getDrawContext().drawHorizontalLine(-size, size, size, Color.green.getRGB());
            render.getDrawContext().drawVerticalLine(  -size,-size, size, Color.green.getRGB());
            render.getDrawContext().drawVerticalLine(   size,-size, size, Color.green.getRGB());

            RenderHelper.drawSmoothRectOutline(Color.green, render.getDrawContext().getMatrices(), -blastRadius, -blastRadius, blastRadius, blastRadius, blastRadius, new int[] {5, 5, 5, 5});

            render.getDrawContext().drawVerticalLine(0, -size-size*2, size-size*2, Color.green.getRGB());
            render.getDrawContext().getMatrices().pop();
            render.getDrawContext().getMatrices().peek().getPositionMatrix().rotate((float) (-Math.toRadians(getYaw()) - Math.atan2(mc.player.getVelocity().normalize().x, mc.player.getVelocity().normalize().z)), 0, 0, 1);
            render.getDrawContext().drawVerticalLine(0, -size-size*2, size-size*2, Color.green.getRGB());
            render.getDrawContext().getMatrices().pop();


        }

        if(cameraDistortion.getValue() && event instanceof EventRender3d) {
            Falsify.shaderManager.DISTORTION.renderShader(Falsify.mc.getFramebuffer().getColorAttachment());
        }
    }

    private double dropTime(double distance) {
        return (0.5*distance + 8*Math.log(0.5*distance + 0.6145) + 3.9) / 20.0;
    }
}
