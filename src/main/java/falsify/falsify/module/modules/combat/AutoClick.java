package falsify.falsify.module.modules.combat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.render.CPSModule;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.Timer;
import net.minecraft.util.hit.HitResult;

public class AutoClick extends Module {
    final RangeSetting aps = new RangeSetting("CPS", 5, 0, 20, 1);
    final RangeSetting randomness = new RangeSetting("Randomness",1, 0, 5, 0.1);
    final RangeSetting windupTime = new RangeSetting("Wind-up",500, 0, 10000, 10);
    final BooleanSetting whileClicking = new BooleanSetting("While Click", true);
    final BooleanSetting trigger = new BooleanSetting("Trigger", false);
    final BooleanSetting waitForCharge = new BooleanSetting("Charge", false);
    double windup = 0.0d;
    final Timer timer = new Timer();
    final Timer windupTimer = new Timer();
    public AutoClick() {
        super("Auto Click", "Automatically click.", true, Category.COMBAT, -1);
        settings.add(aps);
        settings.add(randomness);
        settings.add(windupTime);
        settings.add(whileClicking);
        settings.add(trigger);
        settings.add(waitForCharge);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(windupTime.getValue() == 0.0d) windup = 1.0;
            else if(windupTimer.hasTimeElapsed(1000/20, true)) {
                if (whileClicking.getValue()) {
                    if(mc.options.attackKey.isPressed()) windup = (windup > 1.0d) ? 1.0d : windup + 20 / windupTime.getValue();
                    else windup = (windup < 0.0d) ? 0.0d : windup - 20 / windupTime.getValue();
                } else {
                    windup = 1.0;
                }
            }
            long randomTime = (long) (windup * (Math.random() * randomness.getValue() - randomness.getValue()/2 + aps.getValue()));
            if(waitForCharge.getValue()){
                if(mc.player.getAttackCooldownProgress(0.0f) >= 0.99) {
                    if(!trigger.getValue()) {
                        if (!whileClicking.getValue()) leftClick();

                        else if (mc.options.attackKey.isPressed()) leftClick();
                    } else if(mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                        if (!whileClicking.getValue()) leftClick();
                        else if (mc.options.attackKey.isPressed()) leftClick();
                    }
                }
            } else if(timer.hasTimeElapsed(1000 / ((randomTime == 0) ? 1 : randomTime), true)) {
                if(!trigger.getValue()) {
                    if (!whileClicking.getValue()) leftClick();
                    else if (mc.options.attackKey.isPressed()) leftClick();
                } else if(mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                    if (!whileClicking.getValue()) leftClick();
                    else if (mc.options.attackKey.isPressed()) leftClick();
                }
            }
        }
    }

    private void leftClick() {
        ((MixinMinecraft)mc).leftClick();
        CPSModule.leftClick();
    }
}
