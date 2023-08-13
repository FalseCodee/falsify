package me.falsecode.netty.packet.packets.s2c;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ClientPacketListener;

public class PlayerMessagePacket implements Packet<ClientPacketListener> {
    private final String fromUser;
    private final String content;

    public PlayerMessagePacket(String fromUser, String content) {
        this.fromUser = fromUser;
        this.content = content;
    }

    @Override
    public void apply(ClientPacketListener packetListener) {
        packetListener.onPlayerMessage(this);
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return fromUser;
    }
}
