package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

import java.util.LinkedHashSet;

public class PingSpoof extends Module {
    private final LinkedHashSet<PacketDelay> delayQueue = new LinkedHashSet<>();
    private final RangeSetting ping = new RangeSetting("Ping", 500, 10, 5000, 5);
    public PingSpoof() {
        super("Ping Spoof", "Sets a fake ping.", true, Category.MISC, -1);
        settings.add(ping);
    }

    @Override
    public void onDisable() {
        delayQueue.forEach(delayQueue -> delayQueue.packet.apply(mc.getNetworkHandler()));
        delayQueue.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketRecieve eventPacketRecieve) {
            Packet<?> packet = eventPacketRecieve.getPacket();
            if(packet instanceof KeepAliveS2CPacket || packet instanceof CommonPingS2CPacket) {
                event.setCancelled(true);
                Packet<ClientCommonPacketListener> p1 = (Packet<ClientCommonPacketListener>) packet;
                delayQueue.add(new PacketDelay(p1, System.currentTimeMillis()));
            }
        } else if(event instanceof EventUpdate) {
            delayQueue.removeIf(packetDelay -> {
               if(System.currentTimeMillis() - packetDelay.time > ping.getValue()) {
                   packetDelay.packet.apply(mc.getNetworkHandler());
                   return true;
               }
               return false;
            });
        }
    }

    private record PacketDelay(Packet<ClientCommonPacketListener> packet, long time) {
    }
}
