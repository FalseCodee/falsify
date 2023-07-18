package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.network.packet.Packet;

public class EventPacketSend extends Event<EventPacketSend> {
    public final Packet<?> packet;

    public EventPacketSend(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
