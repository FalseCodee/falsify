package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprint", true, Category.MOVEMENT, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            SafeWalk sw = ModuleManager.getModule(SafeWalk.class);
            mc.player.setSprinting(!((!mc.player.input.hasForwardMovement() || !(mc.player.hasVehicle() || (float)mc.player.getHungerManager().getFoodLevel() > 6.0f || mc.player.getAbilities().allowFlying)) || mc.player.horizontalCollision && !mc.player.collidedSoftly || mc.player.isTouchingWater() && !mc.player.isSubmergedInWater()));
        }
    }
}
