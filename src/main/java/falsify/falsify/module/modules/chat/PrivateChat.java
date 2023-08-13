package falsify.falsify.module.modules.chat;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import me.falsecode.netty.packet.packets.c2s.SendPlayerMessagePacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

public class PrivateChat extends Module {
    public PrivateChat() {
        super("Private Chat", "Enables chatting with other Legacy Client users.", false, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend e) {
            if(e.getPacket() instanceof ChatMessageC2SPacket packet && mc.player != null) {
                if(packet.chatMessage().startsWith(".msg ")) {
                    String msg = packet.chatMessage().substring(".msg ".length());
                    String target = msg.split(" ")[0];
                    msg = msg.substring(target.length()).trim();
                    e.setCancelled(true);

                    Falsify.mc.player.sendMessage(Text.of("§7[DM to §f"+ target +"§7] §f" + msg));
                    Falsify.client.send(new SendPlayerMessagePacket(target, mc.player.getGameProfile().getName(), msg));
                }
            }
        }
    }
}
