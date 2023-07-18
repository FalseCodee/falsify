package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Trajectories extends Module {

    private final Timer timer = new Timer();
    public static AimbotTarget target;
    private final PID pid = new PID(1,0.3f,10);
    private final RangeSetting kp = new RangeSetting("kp", 1, 0, 10, 0.01);
    private final RangeSetting ki = new RangeSetting("ki", 0.3, 0, 10, 0.01);
    private final RangeSetting kd = new RangeSetting("kd", 10, 0, 10, 0.01);
    private final RangeSetting deadzone = new RangeSetting("deadzone", 1, 0, 5, 0.01);
    private final RangeSetting mps = new RangeSetting("Mov.PS", 50, 0, 500, 1);

    private final BooleanSetting sneak = new BooleanSetting("Sneak Close", true);
    private final BooleanSetting doVert = new BooleanSetting("Do Vertical", true);

    public Trajectories() {
        super("Trajectories", "Navigates to coordinates, used by other mods.", true, Category.PLAYER, -1);
        settings.add(kp);
        settings.add(ki);
        settings.add(kd);
        settings.add(deadzone);
        settings.add(sneak);
        settings.add(mps);
        settings.add(doVert);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate e && timer.hasTimeElapsed((long) (1000/mps.getValue()), true) && target != null) {
            calculateLeftRightMovement((target != null) ? target.getLocation() : null);
            calculateForwardBackwardMovement((target != null) ? target.getLocation() : null);
            calculateUpDownMovements((target != null) ? target.getLocation() : null);
        } else if (target == null) {
            mc.options.rightKey.setPressed(false);
            mc.options.leftKey.setPressed(false);
            mc.options.backKey.setPressed(false);
            mc.options.forwardKey.setPressed(false);
            mc.options.sneakKey.setPressed(false);
            mc.options.jumpKey.setPressed(false);
        }
    }

    private void calculateLeftRightMovement(Vec3d target) {
        if(target == null) return;
        Vec2f pos = new Vec2f((float)mc.player.getX(), (float)mc.player.getZ());
        float yaw = (float)Math.toRadians(mc.player.getYaw());
        float targetYaw = (float) Math.toRadians(MathUtils.getRotationsNeeded(target.x, target.y, target.z)[0]);
        float theta = yaw - targetYaw;
        double distance = Math.sqrt((pos.x-target.getX())*(pos.x-target.getX()) + (pos.y-target.getZ())*(pos.y-target.getZ()));

        float error = pid.calcPID((float) (Math.sin(theta)*distance));
        if(error < -deadzone.getValue()) {
            mc.options.leftKey.setPressed(false);
            mc.options.rightKey.setPressed(true);
        } else if(error > deadzone.getValue()) {
            mc.options.rightKey.setPressed(false);
            mc.options.leftKey.setPressed(true);
        } else {
            mc.options.rightKey.setPressed(false);
            mc.options.leftKey.setPressed(false);
        }
    }
    private void calculateForwardBackwardMovement(Vec3d target) {
        if(target == null) return;
        Vec2f pos = new Vec2f((float)mc.player.getX(), (float)mc.player.getZ());
        float yaw = (float)Math.toRadians(mc.player.getYaw());
        float targetYaw = (float) Math.toRadians(MathUtils.getRotationsNeeded(target.x, target.y, target.z)[0]);
        float theta = yaw - targetYaw;
        double distance = Math.sqrt((pos.x-target.getX())*(pos.x-target.getX()) + (pos.y-target.getZ())*(pos.y-target.getZ()));

        mc.options.sneakKey.setPressed(sneak.getValue() && distance < 0.5);


        float error = pid.calcPID((float) (Math.cos(theta)*distance));
        if(error < -deadzone.getValue()) {
            mc.options.forwardKey.setPressed(false);
            mc.options.backKey.setPressed(true);
        } else if(error > deadzone.getValue()) {
            mc.options.backKey.setPressed(false);
            mc.options.forwardKey.setPressed(true);
        } else {
            mc.options.backKey.setPressed(false);
            mc.options.forwardKey.setPressed(false);
        }
    }

    public void calculateUpDownMovements(Vec3d target) {
        if(target == null) return;
        if (doVert.getValue()) {
            if (Math.abs(MathUtils.verticalDistance(target)) > deadzone.getValue()*2) {
                if (MathUtils.verticalDistance(target) > 0) {
                    mc.options.sneakKey.setPressed(true);
                    mc.options.jumpKey.setPressed(false);
                } else {
                    mc.options.jumpKey.setPressed(true);
                    mc.options.sneakKey.setPressed(false);
                }
            } else {
                mc.options.sneakKey.setPressed(false);
                mc.options.jumpKey.setPressed(false);
            }
        }
    }


}
