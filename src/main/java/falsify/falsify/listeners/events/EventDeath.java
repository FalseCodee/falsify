package falsify.falsify.listeners.events;

import com.mojang.authlib.GameProfile;
import falsify.falsify.listeners.Event;
import net.minecraft.entity.damage.DamageSource;

public class EventDeath extends Event<EventDeath> {
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    private final GameProfile gameProfile;

    public EventDeath(GameProfile gameProfile, DamageSource damageSource) {
        this.gameProfile = gameProfile;
        this.damageSource = damageSource;
    }

    private final DamageSource damageSource;

}
