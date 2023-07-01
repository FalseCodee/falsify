package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import org.lwjgl.glfw.GLFW;

public class ChatBot extends Module {
    public ChatBot() {
        super("ChatBot", Category.MISC, GLFW.GLFW_KEY_APOSTROPHE);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketRecieve eventPacketRecieve) {
            if(eventPacketRecieve.getPacket() instanceof ChatMessageS2CPacket packet) {
                String message = packet.unsignedContent().getString().toLowerCase();
                if(!message.startsWith(mc.player.getGameProfile().getName().toLowerCase())) return;

                message = message.substring(mc.player.getGameProfile().getName().length() + 1);


            }
        }
    }

    private void flipCoin(String message) {
        if(!message.startsWith("flipcoin")) return;

        ChatModuleUtils.sendMessage((Math.random() > 0.5) ? "Heads" : "Tails", false);
    }

    private void roll(String message) {
        if(!message.startsWith("roll")) return;
        int amt = 6;
        try {
            amt = Integer.parseInt(message.substring("roll ".length()));
        } catch (NumberFormatException | IndexOutOfBoundsException ignored){}

        ChatModuleUtils.sendMessage((int) (Math.random() * amt) + 1 + "", false);
    }


}
