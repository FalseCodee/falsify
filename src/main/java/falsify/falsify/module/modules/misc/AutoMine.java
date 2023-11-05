package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.Timer;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoMine extends Module {

    private final RangeSetting durability = new RangeSetting("Stop if durability", 10, 0, 1000, 1);

    public AutoMine() {
        super("Auto Mine", "Automatically mines.", true, Category.MISC, -1);
        settings.add(durability);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        if(mc.world == null || mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        int duraLeft = mc.player.getStackInHand(mc.player.getActiveHand()).getMaxDamage() - mc.player.getStackInHand(mc.player.getActiveHand()).getDamage();
        mc.options.attackKey.setPressed(duraLeft > durability.getValue());
    }
}
