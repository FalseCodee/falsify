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
        return getRotationsNeeded(entity.getX(), entity.getBoundingBox().getCenter().getY(), entity.getZ());
    }
    public static float[] getRotationsNeeded(Vec3d pos) {
        return getRotationsNeeded(pos.getX(), pos.getY(), pos.getZ());
    }
    public static float[] getRotationsNeeded(double x, double y, double z) {
        double diffX = x - Falsify.mc.gameRenderer.getCamera().getPos().getX();
        double diffY;

       diffY = y - Falsify.mc.gameRenderer.getCamera().getPos().getY();
        double diffZ = z - Falsify.mc.gameRenderer.getCamera().getPos().getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] {Falsify.mc.gameRenderer.getCamera().getYaw() + MathHelper.wrapDegrees(yaw - Falsify.mc.gameRenderer.getCamera().getYaw()),
                Falsify.mc.gameRenderer.getCamera().getPitch() + MathHelper.wrapDegrees(pitch - Falsify.mc.gameRenderer.getCamera().getPitch())};

    }

    public static float squaredCursorDistanceTo(Entity entity) {
        float[] rotation = getRotationsNeeded(entity);
        rotation[0] -= Falsify.mc.player.getYaw();
        rotation[1] -= Falsify.mc.player.getPitch();

        return rotation[0]*rotation[0] + rotation[1]*rotation[1];
    }
    public static float cursorDistanceTo(Entity entity) {
        return (float) Math.sqrt(squaredCursorDistanceTo(entity));
    }

    public static float lerp(float a, float b, float t){
        return  (a+(b-a)*t);
    }
    public static double lerp(double a, double b, double t){
        return (a+(b-a)*t);
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

    public static double horizontalDistance(Entity entity) {
        return horizontalDistance(entity.getPos());
    }

    public static double horizontalDistance(Vec3d pos) {
        return Math.sqrt((Falsify.mc.player.getX() - pos.getX()) * (Falsify.mc.player.getX() - pos.getX()) + (Falsify.mc.player.getZ() - pos.getZ()) * (Falsify.mc.player.getZ() - pos.getZ()));
    }

    public static double verticalDistance(Entity entity) {
        return verticalDistance(entity.getPos());
    }

    public static double verticalDistance(Vec3d pos) {
        return Falsify.mc.player.getY() - pos.getY();
    }

    public static int getVerticalMov() {
        if(Falsify.mc.options.jumpKey.isPressed() && Falsify.mc.options.sneakKey.isPressed()) return 0;
        else if(Falsify.mc.options.jumpKey.isPressed()) return 1;
        else if(Falsify.mc.options.sneakKey.isPressed()) return -1;
        return 0;
    }

    public static Vec3d pitchYawToVector3d(double pitch, double yaw) {
        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);
        return new Vec3d(Math.cos(yaw+180)*Math.cos(pitch+45), Math.sin(pitch+45), Math.sin(yaw+180)*Math.cos(pitch+45));
    }

    public static float[] toScreenXY(Vec3d pos) {
        float[] rotation = getRotationsNeeded(pos);
        rotation[0] -= Falsify.mc.player.getYaw();
        rotation[1] -= Falsify.mc.player.getPitch();

        rotation[0] /= Falsify.mc.options.getFov().getValue();
        rotation[1] /= ((Falsify.mc.options.getFov().getValue())/RenderUtils.windowRatio);

        rotation[0] = (rotation[0]-0.5f) * Falsify.mc.getWindow().getScaledWidth()/2.0f + 3.0f*Falsify.mc.getWindow().getScaledWidth()/4.0f;
        rotation[1] = (rotation[1]-0.5f) * Falsify.mc.getWindow().getScaledHeight()/2.0f + 3.0f*Falsify.mc.getWindow().getScaledHeight()/4.0f;

        return rotation;
    }

    public static double random(double min, double max) {
        return (Math.random() * (max-min)) + min;
    }

    public static int[] RGBIntToRGB(int in) {
        int red = in >> 8 * 2 & 0xFF;
        int green = in >> 8 & 0xFF;
        int blue = in & 0xFF;
        return new int[]{red, green, blue};
    }
}
