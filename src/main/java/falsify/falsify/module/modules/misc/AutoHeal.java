package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.Timer;

public class AutoHeal extends Module {

    private final RangeSetting delay = new RangeSetting("Delay", 500, 10, 5000, 10);
    private final RangeSetting hunger = new RangeSetting("Hunger Left", 6, 0, 20, 1);
    private final Timer timer = new Timer();
    public AutoHeal() {
        super("Auto Heal", "Automatically /sethomes and /suicides to heal.", true, Category.MISC, -1);
        settings.add(delay);
        settings.add(hunger);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        if(mc.player == null) return;
        if(mc.player.getHungerManager().getFoodLevel() > hunger.getValue().intValue()) return;
        if(!timer.hasTimeElapsed(delay.getValue().longValue()*2L, true)) return;

        ChatModuleUtils.sendMessage("/sethome", false);
        new FalseRunnable() {
            @Override
            public void run() {
                ChatModuleUtils.sendMessage("/suicide", false);
            }
        }.runTaskLater(delay.getValue().longValue());
    }
}
