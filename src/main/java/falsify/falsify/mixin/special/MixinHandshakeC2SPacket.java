package falsify.falsify.mixin.special;


import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandshakeC2SPacket.class)
public interface MixinHandshakeC2SPacket {
    @Accessor("address")
    @Mutable
    void setAddress(String address);

    @Accessor("address")
    @Mutable
    String getAddress();
}
