package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float[] getRotationsNeeded(Entity entity) {
        return getRotationsNeeded(entity.getX(), entity.getY()+1, entity.getZ());
    }
    public static float[] getRotationsNeeded(double x, double y, double z) {
        double diffX = x - Falsify.mc.player.getX();
        double diffY;

       diffY = y - (Falsify.mc.player.getY()+Falsify.mc.player.getEyeHeight(Falsify.mc.player.getPose()));
        double diffZ = z - Falsify.mc.player.getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] {Falsify.mc.player.getYaw() + MathHelper.wrapDegrees(yaw - Falsify.mc.player.getYaw()),
                Falsify.mc.player.getPitch() + MathHelper.wrapDegrees(pitch - Falsify.mc.player.getPitch())};

    }

    public static float cursorDistanceTo(Entity entity) {
        float[] rotation = getRotationsNeeded(entity);
        rotation[0] -= Falsify.mc.player.getYaw();
        rotation[1] -= Falsify.mc.player.getPitch();

        return (float) Math.sqrt(rotation[0]*rotation[0] + rotation[1]*rotation[1]);
    }

    public static float lerp(float a, float b, double t){
        return (float) (a+(b-a)*t);
    }

    public static Vec3d interpolateVec3d(Vec3d current, Vec3d last, float partialTicks) {
        return current.subtract(last).multiply(partialTicks).add(last);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return interpolateVec3d(entity.getPos(), new Vec3d(entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ), partialTicks);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastRenderX + (entity.getX() - entity.lastRenderX) * time, entity.lastRenderY + (entity.getY() - entity.lastRenderY) * time, entity.lastRenderZ + (entity.getZ() - entity.lastRenderZ) * time);
    }

    public static double[] directionSpeed(double speed) {
        float forward = Falsify.mc.player.input.movementForward;
        float side = Falsify.mc.player.input.movementSideways;
        float yaw = Falsify.mc.player.prevYaw + (Falsify.mc.player.getYaw() - Falsify.mc.player.prevYaw) * Falsify.mc.getTickDelta();
        if (forward != 0.0F) {
            if (side > 0.0F) {
                yaw += ((forward > 0.0F) ? -45 : 45);
            } else if (side < 0.0F) {
                yaw += ((forward > 0.0F) ? 45 : -45);
            }
            side = 0.0F;
            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }
        double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
        double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
        double posX = forward * speed * cos + side * speed * sin;
        double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }

    public static void asInt(String s){
        try{
            int number = Integer.parseInt(s);
            //not fail
        }catch(NumberFormatException e){
            return;
        }
    }

    public static double horizontalDistance(Entity entity) {
        return Math.sqrt((Falsify.mc.player.getX() - entity.getX()) * (Falsify.mc.player.getX() - entity.getX()) + (Falsify.mc.player.getZ() - entity.getZ()) * (Falsify.mc.player.getZ() - entity.getZ()));
    }

    public static double verticalDistance(Entity entity) {
        return Falsify.mc.player.getY() - entity.getY();
    }
}
