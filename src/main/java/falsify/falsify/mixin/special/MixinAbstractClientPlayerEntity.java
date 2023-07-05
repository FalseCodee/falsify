package falsify.falsify.mixin.special;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayerEntity {

    private static Identifier devCape;
    @Inject(method = "getCapeTexture", at=@At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir, PlayerListEntry playerListEntry){
        if(playerListEntry.getProfile().getId().equals(Falsify.mc.player.getGameProfile().getId())){
            if(devCape == null) devCape = Falsify.textureCacheManager.getIdentifier("dev_cape");
            cir.setReturnValue(devCape);
        }
    }
}
