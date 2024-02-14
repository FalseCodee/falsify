package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class EntityDeathEvent extends Event<EntityDeathEvent> {
    private final DamageSource source;
    private final LivingEntity victim;

    public EntityDeathEvent(DamageSource source, LivingEntity victim) {
        this.source = source;
        this.victim = victim;
    }

    public DamageSource getSource() {
        return source;
    }

    public LivingEntity getVictim() {
        return victim;
    }
}
