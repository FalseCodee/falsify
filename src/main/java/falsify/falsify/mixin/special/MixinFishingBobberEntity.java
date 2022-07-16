package falsify.falsify.mixin.special;

import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingBobberEntity.class)
public class MixinFishingBobberEntity {
    @ModifyVariable(method = "use", at = @At(value = "STORE"))
    public int onUse(int i){
        return 0;
    }

}
