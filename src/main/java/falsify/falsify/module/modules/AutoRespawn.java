package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventDeath;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("Auto Respawn", Category.MISC, GLFW.GLFW_KEY_U, true);
    }
    public Timer timer = new Timer();
    public boolean dead = false;
    @Override
    public void onEvent(Event e) {
         if(e instanceof EventUpdate){
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
