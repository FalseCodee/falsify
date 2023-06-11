package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.network.packet.Packet;

public class EventPacketRecieve extends Event<EventPacketRecieve> {
    public final Packet<?> packet;

    public EventPacketRecieve(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }




}
