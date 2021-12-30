package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.network.NetworkState;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

public class EventHandshake extends Event<EventHandshake> {

    public EventHandshake(int port, String address, NetworkState intendedState) {
        this.port = port;
        this.address = address;
        this.intendedState = intendedState;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int port;



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public NetworkState getIntendedState() {
        return intendedState;
    }

    public void setIntendedState(NetworkState intendedState) {
        this.intendedState = intendedState;
    }

    public String address;
    public NetworkState intendedState;





}
