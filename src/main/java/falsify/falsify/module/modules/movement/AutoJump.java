package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;

public class AutoJump extends Module {
    private final BooleanSetting whileRunning = new BooleanSetting("Running", false);
    public AutoJump() {
        super("Auto Jump", "Automatically Jump.", true, Category.MOVEMENT, -1);
        settings.add(whileRunning);
    }

    @Override
    public void onEvent(Event<?> event) {
       if(event instanceof EventUpdate) {
           if(whileRunning.getValue()) {
               if (mc.player.input.hasForwardMovement()) {
                   if(mc.player.isOnGround()) mc.player.jump();
               }
           } else {
               if(mc.player.isOnGround()) mc.player.jump();

           }
       }
    }
}
