package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class ChatBot extends Module {
    public ChatBot() {
        super("ChatBot", "Become a chat bot!", false, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketRecieve eventPacketRecieve) {
            if(eventPacketRecieve.getPacket() instanceof GameMessageS2CPacket packet) {
                String message = ChatModuleUtils.concatArray(packet.content().withoutStyle(), "");
                if(!message.contains("CHAT GAME") || !message.contains("First to type word")) return;

                message = message.substring("\nCHAT GAME   First to type word \"".length(), message.length()-3);

                ChatModuleUtils.sendMessage(message, false);
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
