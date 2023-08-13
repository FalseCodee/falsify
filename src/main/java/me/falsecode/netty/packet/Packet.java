package me.falsecode.netty.packet;

import me.falsecode.netty.packet.listeners.PacketListener;

import java.io.Serializable;

public interface Packet<T extends PacketListener> extends Serializable {
    void apply(T packetListener);
}