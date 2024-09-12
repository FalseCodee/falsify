package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.EventType;
import falsify.falsify.listeners.events.EventMovementTick;
import falsify.falsify.utils.FalseRunnable;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void onPreMove(CallbackInfo ci) {
        List<FalseRunnable> tickTasks = new ArrayList<>(FalseRunnable.nextTick);
        for (FalseRunnable runnable : tickTasks) {
            FalseRunnable.nextTick.remove(runnable);
            runnable.run();
        }

        EventMovementTick motion = new EventMovementTick();
        motion.setEventType(EventType.PRE);
        Falsify.onEvent(motion);
        if(motion.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onPostMove(CallbackInfo ci) {
        EventMovementTick motion = new EventMovementTick();
        motion.setEventType(EventType.POST);
        Falsify.onEvent(motion);

    }

    @Mixin(ClientPlayerEntity.class)
    public interface MixinAccessor {
        @Accessor("ticksToNextAutojump")
        @Mutable
        void setTicks(int ticks);
    }

}
