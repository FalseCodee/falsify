package me.falsecode.netty.packet.packets.s2c;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ClientPacketListener;

public class ServerNotificationPacket implements Packet<ClientPacketListener> {
    private final String content;

    public ServerNotificationPacket(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void apply(ClientPacketListener packetListener) {
        packetListener.onServerNotification(this);
    }
}
