package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientBrand extends Module {

    public String brand = "Legacy Client";

    public ClientBrand() {
        super("Brand", Category.MISC, GLFW.GLFW_KEY_BACKSLASH, true);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend eventPacketSend) {
            if(eventPacketSend.packet instanceof ChatMessageC2SPacket packet) {
                if(packet.chatMessage().toLowerCase().startsWith(".brand ")) {
                    brand = packet.chatMessage().substring(7);
                    mc.player.sendMessage(Text.of("Set client brand to: " + brand), false);
                    event.setCancelled(true);
                }
            }
        }
    }
}
