package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.chat.ProtocolChanger;
import net.minecraft.MinecraftVersion;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftVersion.class)
public class MixinSharedConstants {
    @Inject(method = "getProtocolVersion", at = @At("HEAD"), cancellable = true)
    private void changeProtocolVersion(CallbackInfoReturnable<Integer> cir) {
        ProtocolChanger changer = ModuleManager.getModule(ProtocolChanger.class);
        if(changer != null && changer.isEnabled()) cir.setReturnValue(ProtocolChanger.version);
    }
}
