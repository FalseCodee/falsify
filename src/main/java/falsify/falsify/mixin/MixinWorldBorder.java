package falsify.falsify.mixin;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class MixinWorldBorder {
    @Inject(method = "contains(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void onContains(double x, double z, double margin, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(true);
    }
}
