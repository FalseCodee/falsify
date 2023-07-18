package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("Auto Respawn", "Automatically respawn.", true, Category.MISC, -1);
    }
    public final Timer timer = new Timer();
    @Override
    public void onEvent(Event<?> event) {
         if(event instanceof EventUpdate){
             if(timer.hasTimeElapsed(100, true) && mc.player.isDead()) {
                 mc.player.requestRespawn();
             }
        }
    }
}
