package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
    @ModifyVariable(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "HEAD"), argsOnly = true)
    public Packet<?> onSend_ModifyVariable(Packet<?> packet){
        EventPacketSend e = new EventPacketSend(packet);
        Falsify.onEvent(e);

        if(e.isCancelled()){
            return null;
        }
        return e.getPacket();
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSend_Inject(Packet<?> packet, CallbackInfo ci) {
        if(packet == null) ci.cancel();
    }
}
