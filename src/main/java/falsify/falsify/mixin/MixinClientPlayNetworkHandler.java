package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventChunkLoad;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "loadChunk", at = @At("HEAD"))
    private void loadChunk(int x, int z, ChunkData chunkData, CallbackInfo ci) {
        EventChunkLoad event = new EventChunkLoad(x, z, chunkData);
        Falsify.onEvent(event);
    }
}
