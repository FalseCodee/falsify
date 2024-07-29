package falsify.falsify.module.modules.player;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class AutoSell extends Module {
    Timer timer = new Timer();
    public AutoSell() {
        super("Auto Sell", "Automatically Sells Sugarcane", true, Category.PLAYER, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(mc.currentScreen instanceof GenericContainerScreen screen) {
                if(timer.hasTimeElapsed(4000, true)) return;
                if(screen.getScreenHandler().getCursorStack() == ItemStack.EMPTY) {
                    if (!timer.hasTimeElapsed(1000, false)) return;
                    mc.player.sendMessage(Text.of("Thing 1"));
                    mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, 1, 0, SlotActionType.PICKUP, mc.player);
                }
                if(!timer.hasTimeElapsed(2000, false)) return;
                mc.player.sendMessage(Text.of("Thing 2"));
                mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, 0, 0, SlotActionType.PICKUP_ALL, mc.player);
                if(!timer.hasTimeElapsed(3000, false)) return;
                mc.player.sendMessage(Text.of("Thing 3"));
                screen.close();
            }
        }
    }
}
