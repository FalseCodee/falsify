package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;

public class Trajectories extends Module {

    private final Timer timer = new Timer();

    PID pid = new PID(1,0.3f,10);
    PID pidv = new PID(10,0.3f,0.1f);
    RangeSetting kp = new RangeSetting("kp", 1, 0, 10, 0.01);
    RangeSetting ki = new RangeSetting("ki", 0.3, 0, 10, 0.01);
    RangeSetting kd = new RangeSetting("kd", 10, 0, 10, 0.01);
    RangeSetting deadzone = new RangeSetting("deadzone", 0.1, 0, 5, 0.01);
    RangeSetting dist = new RangeSetting("Distance", 9, 0, 15, 0.5);
    RangeSetting mps = new RangeSetting("Mov.PS", 20, 0, 50, 1);

    public Trajectories() {
        super("Trajectories", Category.PLAYER, -1);
        settings.add(kp);
        settings.add(ki);
        settings.add(kd);
        settings.add(deadzone);
        settings.add(dist);
        settings.add(mps);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate e && timer.hasTimeElapsed((long) (1000/mps.getValue()), true) && Aimbot.target != null) {
            Vec2f pos = new Vec2f((float)mc.player.getX(), (float)mc.player.getZ());
            Vec2f posVector = new Vec2f((float)Math.cos(Math.toRadians(mc.player.getYaw())),
                    (float)Math.sin(Math.toRadians(mc.player.getYaw())));
            Vec3d target = Aimbot.target.getPos();
            pid.setKp(kp.getValue().floatValue());
            pid.setKi(ki.getValue().floatValue());
            pid.setKd(kd.getValue().floatValue());
            pidv.setKp(kp.getValue().floatValue());
            pidv.setKi(ki.getValue().floatValue());
            pidv.setKd(kd.getValue().floatValue());
            float yaw = Aimbot.target.getYaw()-90;
            float pitch = Aimbot.target.getPitch()-90;
            float dist = this.dist.getValue().floatValue();
            Vec2f targetBehind = new Vec2f((float) target.x - (dist * (float)Math.cos(Math.toRadians(yaw))),
                    (float) target.z - (dist * (float)Math.sin(Math.toRadians(yaw))));
            Vec2f subtracted = pos.add(targetBehind.negate()).normalize();
            float error = pid.calcPID(posVector.dot(subtracted));
            if(error < -deadzone.getValue()) {
                mc.options.leftKey.setPressed(true);
                mc.options.rightKey.setPressed(false);
            } else if(error > deadzone.getValue()) {
                mc.options.rightKey.setPressed(true);
                mc.options.leftKey.setPressed(false);
            } else {
                mc.options.rightKey.setPressed(false);
                mc.options.leftKey.setPressed(false);
            }

            float errorV = pidv.calcPID((float) (mc.player.getEyeY() - (target.y - (MathUtils.horizontalDistance(Aimbot.target)*(float)Math.cos(Math.toRadians(pitch))))));
//            if(errorV < -deadzone.getValue()) {
//                mc.options.jumpKey.setPressed(true);
//                mc.options.sneakKey.setPressed(false);
//            } else if(errorV > deadzone.getValue()) {
//                mc.options.jumpKey.setPressed(false);
//                mc.options.sneakKey.setPressed(true);
//            }else {
//                mc.options.jumpKey.setPressed(false);
//                mc.options.sneakKey.setPressed(false);
//            }


            mc.player.sendMessage(Text.of("" + (mc.player.getY() - (target.y - (MathUtils.horizontalDistance(Aimbot.target)*(float)Math.cos(Math.toRadians(pitch)))))), true);
        }
    }


}
