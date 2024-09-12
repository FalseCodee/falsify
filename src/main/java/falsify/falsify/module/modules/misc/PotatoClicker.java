package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.InventoryClickHelper;
import falsify.falsify.utils.Timer;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class PotatoClicker extends Module {
    private final Timer timer = new Timer();
    private final RangeSetting speed = new RangeSetting("Speed", 500, 10, 3000, 10);
    public PotatoClicker() {
        super("Potato Clicker", "Clicks potatos.", false, Category.MISC, -1);
        settings.add(speed);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventUpdate)) return;
        if(!timer.hasTimeElapsed(speed.getValue().longValue(), true)) return;

        if(!(mc.currentScreen instanceof GenericContainerScreen screen)) return;
        clickPotato(screen);
    }

    private void clickPotato(GenericContainerScreen screen) {
        InventoryClickHelper inv = new InventoryClickHelper(screen);
        if(!inv.getTitle().startsWith("Potato Peeling")) return;

        boolean hasBakedPotato = screen.getScreenHandler().slots.stream().anyMatch(slot -> slot.getStack().getItem() == Items.BAKED_POTATO);
        for(int i = 0; i < screen.getScreenHandler().slots.size(); i++) {
            Item item = screen.getScreenHandler().slots.get(i).getStack().getItem();
            if((hasBakedPotato && item == Items.BAKED_POTATO) || (!hasBakedPotato && item == Items.POTATO)) {
                inv.clickSlot(i, 0, SlotActionType.PICKUP);
                return;
            }
        }
    }
}
