package me.falsecode.netty.packet.listeners;

import me.falsecode.netty.packet.packets.c2s.*;

public interface ServerPacketListener extends PacketListener {
    void onHandshake(HandshakePacket packet);
    void onClientDisconnect(ClientDisconnectPacket packet);
    void onJoinServer(JoinServerPacket packet);
    void onSendPlayerMessage(SendPlayerMessagePacket packet);
    void onQueryRequestPlayerList(ClientQueryRequestPlayerListPacket packet);
}
