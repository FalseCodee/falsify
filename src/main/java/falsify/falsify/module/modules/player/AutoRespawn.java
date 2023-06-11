package falsify.falsify.module.modules.player;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;
import org.lwjgl.glfw.GLFW;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("Auto Respawn", Category.MISC, GLFW.GLFW_KEY_U, true);
    }
    public final Timer timer = new Timer();
    @Override
    public void onEvent(Event event) {
         if(event instanceof EventUpdate){
             if(timer.hasTimeElapsed(100, true) && mc.player.isDead()) {
                 mc.player.requestRespawn();
             }
        }
    }
}
