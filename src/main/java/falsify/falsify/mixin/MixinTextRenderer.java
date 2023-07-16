package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public class MixinTextRenderer {

    @Inject(method = "getFontStorage", at = @At("HEAD"), cancellable = true)
    private void fontStorage(Identifier id, CallbackInfoReturnable<FontStorage> cir){
        //if(Falsify.fonts != null) cir.setReturnValue(Falsify.fonts.getFontStorage());
    }
}
