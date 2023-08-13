package me.falsecode.netty.packet.packets.c2s;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ServerPacketListener;

public class ClientDisconnectPacket implements Packet<ServerPacketListener> {
    @Override
    public void apply(ServerPacketListener packetListener) {
        packetListener.onClientDisconnect(this);
    }
}