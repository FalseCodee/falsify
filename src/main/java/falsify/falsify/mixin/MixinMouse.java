package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.EventType;
import falsify.falsify.listeners.events.EventMouse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        EventMouse.Scroll e = new EventMouse.Scroll(window, horizontal, vertical);
        Falsify.onEvent(e);

        if(e.isCancelled()) ci.cancel();
    }

    @ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void onChangeCameraDirection(Args args) {
        EventMouse.MoveIngame e = new EventMouse.MoveIngame(args.get(0), args.get(1));
        e.setEventType(EventType.POST);
        Falsify.onEvent(e);
        args.set(0, e.getHorizontal());
        args.set(1, e.getVertical());
    }

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V", shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangeCamera(CallbackInfo ci, double d, double e, double k, double l, double f, double g, double h, int m) {
        EventMouse.MoveIngame event = new EventMouse.MoveIngame(k, l * m);
        event.setEventType(EventType.PRE);
        Falsify.onEvent(event);
        if(event.isCancelled()) ci.cancel();

    }
}
