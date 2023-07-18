package falsify.falsify.module.modules.chat;

import falsify.falsify.gui.TpAnnoyGUI;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.Timer;

public class TPAnnoy extends ChatModule {

    public static String playerToAnnoy = "";
    public static boolean run = false;
    private final Timer timer = new Timer();
    public TPAnnoy() {
        super("TpAnnoy", "Annoy a target by spamming /tpa and /tpahere.", Category.MISC, -1);
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
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(run && timer.hasTimeElapsed(2500, true)) {
                ChatModuleUtils.sendMessage("/tpa " + playerToAnnoy, false);
                new FalseRunnable() {
                    @Override
                    public void run() {
                        ChatModuleUtils.sendMessage("/tpahere " + playerToAnnoy, false);
                    }
                }.runTaskLater(200);
            }
        }
    }
}
