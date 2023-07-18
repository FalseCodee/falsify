package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

public class ProtocolChanger extends ChatModule {

    public static int version = 763;
    public ProtocolChanger() {
        super("Protocol", "Change your protocol version.", Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend e && e.getPacket() instanceof ChatMessageC2SPacket packet && packet.chatMessage().toLowerCase().startsWith(".protocol ")) {
            version = Integer.parseInt(packet.chatMessage().substring(".protocol ".length()));
            mc.player.sendMessage(Text.of("Set Protocol Version to " + version), false);
            e.setCancelled(true);
        }
    }
}
