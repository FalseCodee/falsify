package falsify.falsify.module.modules.render;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.item.Items;

public class BowTrajectories extends Module {
    public BowTrajectories() {
        super("Bow Trajectories", "Shows where arrows will land.", true, Category.RENDER, -1);
    }


    @Override
    public void onEvent(Event<?> event) {
        if(!mc.player.isUsingItem() || mc.player.getActiveItem().getItem() != Items.BOW) return;

        if(event instanceof EventRender3d eventRender3d) {

        }
    }
}
