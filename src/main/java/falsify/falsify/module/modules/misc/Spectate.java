package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.text.Text;

public class Spectate extends Module {
    private final RangeSetting horizontalCutoff = new RangeSetting("Horizontal", 7, 0, 30, 0.1);
    private final BooleanSetting doVert = new BooleanSetting("Do Vert", false);
    private final RangeSetting verticalCutoff = new RangeSetting("Vertical", 2, 0, 30, 0.1);
    private final BooleanSetting returnToSpawn = new BooleanSetting("/spawn", false);

    private final Timer timer = new Timer();
    public Spectate() {
        super("Spectate", Category.MOVEMENT, -1);
        settings.add(horizontalCutoff);
        settings.add(doVert);
        settings.add(verticalCutoff);
        settings.add(returnToSpawn);
    }

    @Override
    public void onEvent(Event<?> event) {

        if(event instanceof EventUpdate) {
            if (Aimbot.target == null) {
                if (returnToSpawn.getValue()) {
                    if (timer.hasTimeElapsed(5000, true)) mc.player.sendMessage(Text.of("/home prime"));
                }
                return;
            }
            mc.options.forwardKey.setPressed(MathUtils.horizontalDistance(Aimbot.target.getEntity()) > horizontalCutoff.getValue());

            if (doVert.getValue()) {
                if (Math.abs(MathUtils.verticalDistance(Aimbot.target.getEntity())) > verticalCutoff.getValue()) {
                    if (MathUtils.verticalDistance(Aimbot.target.getEntity()) > 0) {
                        mc.options.sneakKey.setPressed(true);
                        mc.options.jumpKey.setPressed(false);
                    } else {
                        mc.options.jumpKey.setPressed(true);
                        mc.options.sneakKey.setPressed(false);
                    }
                } else {
                    mc.options.sneakKey.setPressed(false);
                    mc.options.jumpKey.setPressed(false);
                }
            }
        }
    }
}
