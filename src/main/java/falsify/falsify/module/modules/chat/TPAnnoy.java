package falsify.falsify.module.modules.chat;

import falsify.falsify.gui.TpAnnoyGUI;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.Timer;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class TPAnnoy extends ChatModule {

    public static String playerToAnnoy = "";
    public static boolean run = false;
    private final Timer timer = new Timer();
    public TPAnnoy() {
        super("TpAnnoy", Category.MISC, GLFW.GLFW_KEY_K);
    }

    @Override
    public void onEnable() {
        mc.setScreen(new TpAnnoyGUI(mc.currentScreen));
    }

    @Override
    public void onDisable() {
        run = false;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(run && timer.hasTimeElapsed(2500, true)) {
                mc.player.sendMessage(Text.of("/tpa " + playerToAnnoy));
                new FalseRunnable() {
                    @Override
                    public void run() {
                        mc.player.sendMessage(Text.of("/tpahere " + playerToAnnoy));
                    }
                }.runTaskLater(200);
            }
        }
    }
}
