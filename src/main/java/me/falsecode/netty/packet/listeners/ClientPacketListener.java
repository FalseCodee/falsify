package me.falsecode.netty.packet.listeners;

import me.falsecode.netty.packet.packets.s2c.PlayerMessagePacket;
import me.falsecode.netty.packet.packets.s2c.ServerNotificationPacket;
import me.falsecode.netty.packet.packets.s2c.ServerQueryResponsePlayerListPacket;

public interface ClientPacketListener extends PacketListener {
    void onServerNotification(ServerNotificationPacket packet);
    void onPlayerMessage(PlayerMessagePacket packet);
    void onServerQueryResponsePlayerList(ServerQueryResponsePlayerListPacket packet);

}
