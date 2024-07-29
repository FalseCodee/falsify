package falsify.falsify.module.modules.player;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Comparator;

public class AutoTool extends Module {
    public AutoTool() {
        super("Auto Tool", "Automatically selects the correct tool.", true, Category.PLAYER, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        if(!mc.options.attackKey.isPressed()) return;
        if(mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        BlockHitResult bhr = (BlockHitResult) mc.crosshairTarget;
        BlockState bs = mc.world.getBlockState(bhr.getBlockPos());

        getBestEquipment(bs);
    }

    private void getBestEquipment(BlockState bs) {
        ItemStack itemStack = mc.player.getInventory().main.stream()
                .filter(is -> is.getItem() instanceof MiningToolItem toolItem && toolItem.isCorrectForDrops(is, bs)).max(Comparator.comparingDouble(is -> is.getComponents().get(DataComponentTypes.TOOL).getSpeed(bs))).orElse(null);
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
