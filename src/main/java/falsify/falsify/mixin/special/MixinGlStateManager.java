package falsify.falsify.mixin.special;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {
    @Inject(method = "_glBindFramebuffer", at = @At("HEAD"))
    private static void bindFbo(int target, int framebuffer, CallbackInfo ci) {
        //System.out.println("bounded fbo: " + framebuffer);
    }
}
