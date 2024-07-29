package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.Timer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class TazCrafterDefamation extends Module {
    private final Timer timer = new Timer();
    private final RangeSetting speed = new RangeSetting("Speed", 100, 1, 1000, 1);
    private int index = 0;

    public TazCrafterDefamation() {
        super("EZ", "TAZCRAFTER EZ EZ EZ EZ EZ", false, Category.MISC, -1);
        settings.add(speed);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;

        if(timer.hasTimeElapsed(speed.getValue().longValue(), true)) {
            if(index == 0 || index == 1) leftClick();
            else if(index == 2) rightClick();
            index = (index + 1) % 3;
        }
    }

    public static void leftClick() {
        HitResult hr = mc.crosshairTarget;
        if(!(hr instanceof BlockHitResult bhr) || bhr.getType() == HitResult.Type.MISS) return;
        mc.interactionManager.attackBlock(bhr.getBlockPos(), bhr.getSide());
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void rightClick() {
        HitResult hr = mc.crosshairTarget;
        if(!(hr instanceof BlockHitResult bhr) || bhr.getType() == HitResult.Type.MISS) return;
        ActionResult ar;
        if((ar = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr)) != null && ar.shouldSwingHand())
            mc.player.swingHand(Hand.MAIN_HAND);
    }
}
