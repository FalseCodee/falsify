package falsify.falsify.module.modules.player;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.Comparator;

public class AutoEat extends Module {
    public AutoEat() {
        super("Auto Eat", "Keeps your belly full!", true, Category.PLAYER, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        int foodNeeded = 20-mc.player.getHungerManager().getFoodLevel();

        if(foodNeeded == 0) return;

        getBestFood(foodNeeded);
        if(!mc.player.getInventory().getMainHandStack().isFood()) return;

        mc.options.useKey.setPressed(true);
        ActionResult ar = mc.interactionManager.interactItem(mc.player, mc.player.getActiveHand());
        if(ar.isAccepted() && ar.shouldSwingHand()) mc.player.swingHand(mc.player.getActiveHand());
    }

    private void getBestFood(int foodNeeded) {
        ItemStack itemStack = mc.player.getInventory().main.stream()
                .filter(is -> is.getItem().isFood() && (is.getItem().getFoodComponent().getHunger()-foodNeeded < 4)).findFirst().orElse(null);
        if(itemStack == null) return;

        int index = mc.player.getInventory().getSlotWithStack(itemStack);
        if(index == -1) mc.player.sendMessage(Text.of(itemStack.getItem().getTranslationKey()));
        else if(index > 9) {
            mc.player.getInventory().selectedSlot = 0;
            mc.player.getInventory().swapSlotWithHotbar(index);
        }
        else mc.player.getInventory().selectedSlot = index;
    }
}
