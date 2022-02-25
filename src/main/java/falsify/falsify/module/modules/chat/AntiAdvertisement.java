package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.module.settings.BooleanSetting;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class AntiAdvertisement extends ChatModule {

    BooleanSetting actionBar = new BooleanSetting("Action Bar", true);
    public AntiAdvertisement() {
        super("AntiAd", Category.MISC, GLFW.GLFW_KEY_APOSTROPHE, true);
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
