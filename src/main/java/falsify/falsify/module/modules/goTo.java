package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.MathUtils;
import org.lwjgl.glfw.GLFW;

public class goTo extends Module {
    public goTo() {
        super("Go To", Category.MOVEMENT, GLFW.GLFW_KEY_M);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate){
            double[] coords = {69, 69 ,69};
            if(Math.sqrt((coords[0]-mc.player.getX())*(coords[0]-mc.player.getX())+(coords[2]-mc.player.getZ())*(coords[2]-mc.player.getZ())) >= 2){
                mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[0], 0.5f));
                mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[1], 0.5f));
                mc.options.forwardKey.setPressed(true);
            } else if(mc.options.forwardKey.isPressed()){
                mc.options.forwardKey.setPressed(false);
            }
        }
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
    }
}
