package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoPlace extends Module {
    public AutoPlace() {
        super("Auto Place", "Automatically Places Blocks.", true, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        if(mc.world == null) return;

        mc.options.useKey.setPressed(true);
    }

    private void placeBlockIfPresent(HitResult result) {
        if(result.getType() != HitResult.Type.BLOCK) return;
        BlockHitResult blockHitResult = (BlockHitResult)result;
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (!mc.world.getBlockState(blockPos).isAir()) {
            mc.interactionManager.attackBlock(blockPos, blockHitResult.getSide());
            if (!mc.world.getBlockState(blockPos).isAir()) return;
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
