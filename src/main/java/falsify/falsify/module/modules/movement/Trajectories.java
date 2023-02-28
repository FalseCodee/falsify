package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.misc.SafeFarm;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Trajectories extends Module {

    private final Timer timer = new Timer();

    PID pid = new PID(1,0.3f,10);
    RangeSetting kp = new RangeSetting("kp", 1, 0, 10, 0.01);
    RangeSetting ki = new RangeSetting("ki", 0.3, 0, 10, 0.01);
    RangeSetting kd = new RangeSetting("kd", 10, 0, 10, 0.01);
    RangeSetting deadzone = new RangeSetting("deadzone", 1, 0, 5, 0.01);
    RangeSetting mps = new RangeSetting("Mov.PS", 20, 0, 50, 1);

    BooleanSetting sneak = new BooleanSetting("Sneak Close", true);

    public Trajectories() {
        super("Trajectories", Category.PLAYER, -1);
        settings.add(kp);
        settings.add(ki);
        settings.add(kd);
        settings.add(deadzone);
        settings.add(sneak);
        settings.add(mps);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate e && timer.hasTimeElapsed((long) (1000/mps.getValue()), true) && SafeFarm.target != null) {
            calculateLeftRightMovement(SafeFarm.target);
            calculateForwardBackwardMovement(SafeFarm.target);
        } else if (SafeFarm.target == null) {
            mc.options.rightKey.setPressed(false);
            mc.options.leftKey.setPressed(false);
            mc.options.backKey.setPressed(false);
            mc.options.forwardKey.setPressed(false);
            mc.options.sneakKey.setPressed(false);
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


}
