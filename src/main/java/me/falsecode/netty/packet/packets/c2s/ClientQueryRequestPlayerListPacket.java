package me.falsecode.netty.packet.packets.c2s;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ServerPacketListener;

import java.util.Set;
import java.util.UUID;

public class ClientQueryRequestPlayerListPacket implements Packet<ServerPacketListener> {
    private final Set<UUID> uuids;

    public ClientQueryRequestPlayerListPacket(Set<UUID> uuids) {
        this.uuids = uuids;
    }

    public Set<UUID> getUuids() {
        return uuids;
    }

    @Override
    public void apply(ServerPacketListener packetListener) {
        packetListener.onQueryRequestPlayerList(this);
    }
}
