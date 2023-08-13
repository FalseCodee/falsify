package me.falsecode.netty.packet.packets.c2s;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ServerPacketListener;

import java.util.UUID;

public class HandshakePacket implements Packet<ServerPacketListener> {
    private final UUID playerUuid;
    private final String playerName;

    public HandshakePacket(UUID playerUuid, String playerName) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }

    @Override
    public void apply(ServerPacketListener packetListener) {
        packetListener.onHandshake(this);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }
}
