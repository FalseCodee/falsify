package falsify.falsify.module.modules;

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
    public boolean dead = false;
    @Override
    public void onEvent(Event event) {
         if(event instanceof EventUpdate){
             if(!dead && mc.player.isDead()){
                 timer.reset();
                 dead = true;
             }
            else if(timer.hasTimeElapsed(1000*45, true) && mc.player.isDead()){
                 mc.player.requestRespawn();
            }
             else if(dead && !mc.player.isDead()){
                dead = false;
            }
        }
    }
}
