package falsify.falsify.mixin.special;

import falsify.falsify.Falsify;
import falsify.falsify.utils.Cape;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity {

    @Shadow protected abstract @Nullable PlayerListEntry getPlayerListEntry();

    @Inject(method = "getCapeTexture", at=@At("TAIL"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir){
        PlayerListEntry playerListEntry = getPlayerListEntry();
        if(playerListEntry == null) return;
        UUID uuid = playerListEntry.getProfile().getId();
        if(Falsify.playerDataManager.hasCape(uuid)) {
            Cape cape = Cape.getCape(Falsify.playerDataManager.getCape(uuid));
            if(cape == null) return;
            LegacyIdentifier id = cape.getCape();
            if(id == null) return;
            cir.setReturnValue(id);
        }
    }

    @Inject(method = "canRenderCapeTexture", at = @At("TAIL"), cancellable = true)
    private void canRenderCapeTexture(CallbackInfoReturnable<Boolean> cir) {
        PlayerListEntry playerListEntry = getPlayerListEntry();
        if(playerListEntry == null) return;
        UUID uuid = playerListEntry.getProfile().getId();
        if(Falsify.playerDataManager.hasCape(uuid)) {
            Cape cape = Cape.getCape(Falsify.playerDataManager.getCape(uuid));
            if(cape == null) return;
            LegacyIdentifier id = cape.getCape();
            if(id == null) return;
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canRenderElytraTexture", at = @At("TAIL"), cancellable = true)
    private void canRenderElytraTexture(CallbackInfoReturnable<Boolean> cir) {
        PlayerListEntry playerListEntry = getPlayerListEntry();
        if (playerListEntry == null) return;
        UUID uuid = playerListEntry.getProfile().getId();
        if (Falsify.playerDataManager.hasCape(uuid)) {
            Cape cape = Cape.getCape(Falsify.playerDataManager.getCape(uuid));
            if (cape == null) return;
            LegacyIdentifier id = cape.getCape();
            if (id == null) return;
            cir.setReturnValue(true);
        }
    }
}
