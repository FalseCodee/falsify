package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;

public class FastJump extends Module {
    public FastJump() {
        super("FastJump", "Removes the delay between jumps.", false, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(mc.player.input.jumping && mc.player.isOnGround() && !mc.player.getWorld().isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0.0, 1.0, 0.0))) {
                mc.player.jump();
            }
        }
    }
}
