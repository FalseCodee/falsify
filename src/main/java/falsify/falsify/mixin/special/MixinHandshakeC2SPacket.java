package falsify.falsify.mixin.special;


import net.minecraft.network.listener.ServerHandshakePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandshakeC2SPacket.class)
public interface MixinHandshakeC2SPacket extends Packet<ServerHandshakePacketListener> {
    @Accessor("address")
    @Mutable
    void setAddress(String address);

    @Accessor("address")
    @Mutable
    String getAddress();
}
