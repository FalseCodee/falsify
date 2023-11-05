package falsify.falsify.mixin.special;

import falsify.falsify.Falsify;
import falsify.falsify.utils.capes.Cape;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(SkinTextures.class)
public abstract class MixinAbstractClientPlayerEntity {
    @Shadow @Nullable public abstract String textureUrl();

    @Inject(method = "capeTexture", at=@At("TAIL"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir){
//        PlayerListEntry playerListEntry = getPlayerListEntry();
//        if(playerListEntry == null) return;
//        UUID uuid = playerListEntry.getProfile().getId();
//        if(Falsify.playerDataManager.hasCape(uuid)) {
//            Cape cape = Cape.getCape(Falsify.playerDataManager.getCape(uuid));
//            if(cape == null) return;
//            LegacyIdentifier id = cape.getCape();
//            if(id == null) return;
//            cir.setReturnValue(id);
//        }
        Cape cape = Cape.getCape("armorup_cape");
        if(cape == null) return;
        LegacyIdentifier id = cape.getCape();
        if(id == null) return;
        cir.setReturnValue(id);
    }
}
