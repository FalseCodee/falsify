package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;

public class ElytraFly extends Module {
    RangeSetting speed = new RangeSetting("Speed", 1.1, 0.1, 20, 0.01);
    public ElytraFly() {
        super("ElytraFly", "Infinite elytra boost.", true, Category.MOVEMENT, -1);
        settings.add(speed);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventMouse e && mc.player.isFallFlying() && e.button == 1){
            mc.player.setVelocity(mc.player.getVelocity().multiply(speed.getValue()));
        }
    }
}
