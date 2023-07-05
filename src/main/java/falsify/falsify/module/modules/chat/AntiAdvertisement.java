package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.module.settings.BooleanSetting;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AntiAdvertisement extends ChatModule {

    final BooleanSetting actionBar = new BooleanSetting("Action Bar", true);
    public AntiAdvertisement() {
        super("AntiAd","Removes Minehut advertisements.", Category.MISC, -1);
        settings.add(actionBar);
    }

    @Override
    public void onChat(EventPacketRecieve eventPacketRecieve, String message) {
        if(message == null || message.split(" ").length == 0) return;
        if(message.split(" ")[0].toUpperCase().contains("[AD]")) {
            if(actionBar.getValue()) mc.player.sendMessage(Text.of(message), true);
            eventPacketRecieve.setCancelled(true);
        }
    }
}
