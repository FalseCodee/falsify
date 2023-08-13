package me.falsecode.netty.packet.packets.c2s;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ServerPacketListener;

public class JoinServerPacket implements Packet<ServerPacketListener> {
    private final String address;

    public JoinServerPacket(String address) {
        this.address = address;
    }

    @Override
    public void apply(ServerPacketListener packetListener) {
        packetListener.onJoinServer(this);
    }

    public String getAddress() {
        return address;
    }
}
