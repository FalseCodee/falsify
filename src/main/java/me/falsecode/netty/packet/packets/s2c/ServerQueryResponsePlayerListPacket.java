package me.falsecode.netty.packet.packets.s2c;

import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ClientPacketListener;

import java.util.Set;
import java.util.UUID;

public class ServerQueryResponsePlayerListPacket implements Packet<ClientPacketListener> {
    private final Set<UUID> uuids;

    public ServerQueryResponsePlayerListPacket(Set<UUID> uuids) {
        this.uuids = uuids;
    }

    public Set<UUID> getUuids() {
        return uuids;
    }

    @Override
    public void apply(ClientPacketListener packetListener) {
        packetListener.onServerQueryResponsePlayerList(this);
    }
}
