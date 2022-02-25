package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventRender3d;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 1))
    private void onRender3d(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        EventRender3d eventRender3d = new EventRender3d(tickDelta, matrices);
        Falsify.onEvent(eventRender3d);
    }
}
