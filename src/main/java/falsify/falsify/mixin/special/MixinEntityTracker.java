package falsify.falsify.mixin.special;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventTrack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinInner;

import java.util.Set;

@Mixin(ClientWorld.class)
public class MixinEntityTracker {

    @Inject(method = "addEntityPrivate", at = @At(value = "HEAD"))
    public void startTracking(int id, Entity entity, CallbackInfo ci){
        if(entity instanceof PlayerEntity){
            EventTrack track = new EventTrack((PlayerEntity) entity, true);
            Falsify.onEvent(track);
        }
    }

    @Inject(method = "removeEntity", at = @At(value = "HEAD"))
    public void removeEnttiy(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci){
            EventTrack track = new EventTrack(false);
            Falsify.onEvent(track);
    }
}
