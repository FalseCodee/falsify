package falsify.falsify.mixin;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.movement.SafeWalk;
import falsify.falsify.utils.capes.Cape;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Shadow public abstract GameProfile getGameProfile();

    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    private void clipLedge(CallbackInfoReturnable<Boolean> cir) {
        SafeWalk safeWalk = ModuleManager.getModule(SafeWalk.class);
        if(safeWalk != null && safeWalk.isEnabled() && !safeWalk.isSneakMode()) cir.setReturnValue(true);
    }
    @Inject(method = "isPartVisible", at = @At("HEAD"), cancellable = true)
    private void isPartVisible(PlayerModelPart modelPart, CallbackInfoReturnable<Boolean> cir) {
        if(modelPart != PlayerModelPart.CAPE) return;
        GameProfile profile = getGameProfile();
        if(profile == null) return;
        UUID uuid = profile.getId();
        if(Falsify.playerDataManager.hasCape(uuid)) {
            Cape cape = Cape.getCape(Falsify.playerDataManager.getCape(uuid));
            if(cape == null) return;
            LegacyIdentifier id = cape.getCape();
            if(id == null) return;
            cir.setReturnValue(true);
        }
    }
}
