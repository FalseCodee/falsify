package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.EventType;
import falsify.falsify.listeners.events.EventFrame;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void onPreMove(CallbackInfo ci) {
        EventFrame motion = new EventFrame();
        motion.setEventType(EventType.PRE);
        Falsify.onEvent(motion);
        if(motion.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("TAIL"))
    private void onPostMove(CallbackInfo ci) {
        EventFrame motion = new EventFrame();
        motion.setEventType(EventType.POST);
        Falsify.onEvent(motion);
    }

}
