package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventMouse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {
    @Inject(method = "onMouseButton",at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int button, int action, int mods, CallbackInfo ci){
        if(MinecraftClient.getInstance().currentScreen == null){
            EventMouse e = new EventMouse(window, button, action, mods);
            Falsify.onEvent(e);

            if(e.isCancelled()) ci.cancel();
        }
    }
}
