package falsify.falsify.mixin;

import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.render.Fullbright;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;", ordinal = 1))
    public <T> T editGamma(SimpleOption<T> instance) {
        Fullbright fb = ModuleManager.getModule(Fullbright.class);

        return fb.isEnabled() ? (T)fb.getValue() : instance.getValue();
    }
}
