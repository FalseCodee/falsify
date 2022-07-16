package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.network.ClientConnection.class)
public class MixinPacket {
    @Inject(method = "handlePacket", at = @At(value = "HEAD"), cancellable = true)
    private static  <T extends PacketListener> void onPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci){
        EventPacketRecieve e = new EventPacketRecieve(packet);
        Falsify.onEvent(e);
        if(e.isCancelled()){
            ci.cancel();
        }
    }
    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At(value = "HEAD"), cancellable = true)
    public void onSend(Packet<?> packet, CallbackInfo ci){
        EventPacketSend e = new EventPacketSend(packet);
        Falsify.onEvent(e);
        if(e.isCancelled()){
            ci.cancel();
        }
    }

}
