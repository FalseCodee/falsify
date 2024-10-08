package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("Auto Walk", "Automatically Walk", true, Category.MOVEMENT, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
       if(event instanceof EventUpdate) {
           mc.options.forwardKey.setPressed(true);
       }
    }

    @Override
    public void onDisable() {
       mc.options.forwardKey.setPressed(false);
    }
}
