package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.util.math.MathHelper;

public class MathUtils {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    public static float[] getRotationsNeeded(double x, double y, double z) {
        double diffX = x+0.5 - Falsify.mc.player.getX();
        double diffY;

       diffY = y+0.5 - (Falsify.mc.player.getY()+Falsify.mc.player.getEyeHeight(Falsify.mc.player.getPose()));
        double diffZ = z+0.5 - Falsify.mc.player.getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] {Falsify.mc.player.getYaw() + MathHelper.wrapDegrees(yaw - Falsify.mc.player.getYaw()),
                Falsify.mc.player.getPitch() + MathHelper.wrapDegrees(pitch - Falsify.mc.player.getPitch())};

    }
    public static float lerp(float a, float b, float t){
        return a+(b-a)*t;
    }


    public static void asInt(String s){
        try{
            int number = Integer.parseInt(s);
            //not fail
        }catch(NumberFormatException e){
            return;
        }
    }
}
