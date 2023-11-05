package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import net.minecraft.text.Text;

public class FindUnicode extends ChatModule {
    public FindUnicode() {
        super("Find Unicode", "Finds unicode characters", Category.MISC, -1);
    }

    @Override
    public void onChat(EventPacketRecieve eventPacketRecieve, String message) {
        if(mc.player == null) return;

        for(char c : message.toCharArray()) {
            if((int) c > 500) mc.player.sendMessage(Text.of((int) c + ""), false);
        }
    }
}
