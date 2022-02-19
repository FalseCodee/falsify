package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class AntiAdvertisement extends ChatModule {
    public AntiAdvertisement() {
        super("AntiAd", Category.MISC, GLFW.GLFW_KEY_APOSTROPHE, true);
    }

    @Override
    public void onChat(EventPacketRecieve eventPacketRecieve, String message) {
        if(message == null || message.split(" ").length == 0) return;
        if(message.split(" ")[0].toUpperCase().contains("[AD]")) {
            mc.player.sendMessage(Text.of(message), true);
            eventPacketRecieve.setCancelled(true);
        }
    }
}
