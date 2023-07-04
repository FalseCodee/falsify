package falsify.falsify.mixin;

import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {


    @Inject(method = "onWindowSizeChanged", at = @At("HEAD"))
    public void onWindowSizeChanged(long window, int width, int height, CallbackInfo ci){
        RenderUtils.windowRatio = width/(float)height;
    }
}
