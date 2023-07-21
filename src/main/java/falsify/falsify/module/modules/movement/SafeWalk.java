package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;

public class SafeWalk extends Module {
    private final RangeSetting fallDistance = new RangeSetting("Fall Distance", 1, 1, 10, 0.1);
    private final RangeSetting sneakDistance = new RangeSetting("Sneak Distance", 0.01, 0.01, 0.25, 0.01);
    private final BooleanSetting sneak = new BooleanSetting("Sneak", true);
    public SafeWalk() {
        super("SafeWalk", "Prevents you from walking off edges.", true, Category.MOVEMENT, -1);
        settings.add(fallDistance);
        settings.add(sneakDistance);
        settings.add(sneak);
    }

    public boolean isSneakMode() {
        return sneak.getValue();
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(atLedge() && isSneakMode()) mc.player.setSneaking(true);
        }
    }

    public boolean atLedge() {
        return mc.player.isOnGround() &&  mc.player.getWorld().isSpaceEmpty(mc.player, mc.player.getBoundingBox().expand(-sneakDistance.getValue()).offset(0.0, -fallDistance.getValue(), 0.0));
    }
}
