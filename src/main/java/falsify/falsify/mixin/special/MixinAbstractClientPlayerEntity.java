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

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayerEntity {

    private static Identifier devCape;
    private static UUID uuid_FLSE = UUID.fromString("c200922e-4b3a-4917-a393-d8f47d820ebc");
    @Inject(method = "getCapeTexture", at=@At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir, PlayerListEntry playerListEntry){
        if(playerListEntry.getProfile().getId().equals(uuid_FLSE)){
            if(devCape == null) devCape = Falsify.textureCacheManager.getIdentifier("dev_cape");
            cir.setReturnValue(devCape);
        }
    }
}
