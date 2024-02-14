package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EntityDeathEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        EntityDeathEvent event = new EntityDeathEvent(damageSource, ((LivingEntity)(Object)this));
        Falsify.onEvent(event);
    }
}
