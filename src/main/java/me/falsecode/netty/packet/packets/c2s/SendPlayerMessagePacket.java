package me.falsecode.netty.packet.packets.c2s;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ServerPacketListener;

public class SendPlayerMessagePacket implements Packet<ServerPacketListener> {
    private final String targetUser;
    private final String fromUser;
    private final String content;

    public SendPlayerMessagePacket(String targetUser, String fromUser, String content) {
        this.targetUser = targetUser;
        this.fromUser = fromUser;
        this.content = content;
    }

    @Override
    public void apply(ServerPacketListener packetListener) {
        packetListener.onSendPlayerMessage(this);
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return fromUser;
    }

    public String getTargetUser() {
        return targetUser;
    }
}
