package falsify.falsify.mixin;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import falsify.falsify.utils.Cape;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Shadow public abstract GameProfile getGameProfile();

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
