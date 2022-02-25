package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;

import java.text.DecimalFormat;

public class Spectate extends Module {
    private final RangeSetting horizontalCutoff = new RangeSetting("Horizontal", 7, 0, 30, new DecimalFormat("#.#"));
    private final RangeSetting verticalCutoff = new RangeSetting("Vertical", 2, 0, 30, new DecimalFormat("#.#"));
    private final BooleanSetting returnToSpawn = new BooleanSetting("/spawn", true);

    private final Timer timer = new Timer();
    public Spectate() {
        super("Spectate", Category.MOVEMENT, -1);
        settings.add(horizontalCutoff);
        settings.add(verticalCutoff);
        settings.add(returnToSpawn);
    }

    @Override
    public void onEvent(Event<?> event) {

        if(event instanceof EventUpdate) {
            if(Aimbot.target == null) {
                if(returnToSpawn.getValue()) {
                    if(timer.hasTimeElapsed(5000, true)) mc.player.sendChatMessage("/spawn");
                }
                return;
            }
            mc.options.keyForward.setPressed(MathUtils.horizontalDistance(Aimbot.target) > horizontalCutoff.getValue());

            if(Math.abs(MathUtils.verticalDistance(Aimbot.target)) > verticalCutoff.getValue()) {
                if(MathUtils.verticalDistance(Aimbot.target) > 0) {
                    mc.options.keySneak.setPressed(true);
                    mc.options.keyJump.setPressed(false);
                } else {
                    mc.options.keyJump.setPressed(true);
                    mc.options.keySneak.setPressed(false);
                }
            } else {
                mc.options.keySneak.setPressed(false);
                mc.options.keyJump.setPressed(false);
            }
        }
    }
}
