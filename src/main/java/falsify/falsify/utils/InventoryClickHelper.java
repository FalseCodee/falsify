package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryClickHelper {

    private final GenericContainerScreen screen;
    private final String title;

    public InventoryClickHelper(GenericContainerScreen screen) {
        this.screen = screen;
        this.title = ChatModuleUtils.concatArray(screen.getTitle().withoutStyle(),"");
    }

    public void clickSlot(int slot, int button, SlotActionType type) {
        Falsify.mc.interactionManager.clickSlot(Falsify.mc.player.currentScreenHandler.syncId, slot, button, type, Falsify.mc.player);
    }

    public String getTitle() {
        return title;
    }
}
