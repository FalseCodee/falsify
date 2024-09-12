package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.chat.TazCrafterDefamation;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.InventoryClickHelper;
import falsify.falsify.utils.Timer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class MelonCrafter extends Module {

    private RecipeEntry<?> melonRecipe;
    private final Timer timer = new Timer();
    private final RangeSetting speed = new RangeSetting("Buy Speed", 200, 10, 1000, 10);
    private boolean shouldCraft = true;
    public MelonCrafter() {
        super("Melon Crafter", "Melon", true, Category.MISC, -1);
        settings.add(speed);
    }

    @Override
    public void onEnable() {
        shouldCraft = true;
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(melonRecipe == null) melonRecipe = mc.world.getRecipeManager().get(Identifier.of("melon")).get();
            else if(shouldCraft) {
                shouldCraft = tryCraft();
            } else {
                if(!timer.hasTimeElapsed(speed.getValue().longValue(), true)) return;
                if(!trySell()) shouldCraft = tryBuy();
            }
        }
    }

    public boolean tryCraft() {
        if(!timer.hasTimeElapsed(500, true)) return true;
        if (mc.currentScreen instanceof CraftingScreen) {
            if (mc.player.getInventory().main.stream()
                    .noneMatch(itemStack -> itemStack.getItem() == Items.MELON_SLICE && itemStack.getCount() >= 9)) {
                mc.setScreen(null);
                return false;
            }

            mc.interactionManager.clickRecipe(mc.player.currentScreenHandler.syncId, melonRecipe, true);
            new FalseRunnable() {
                @Override
                public void run() {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 0, 1, SlotActionType.QUICK_MOVE, mc.player);
                }
            }.runTaskLater(200);

        } else {
            TazCrafterDefamation.rightClick();
            return true;
        }
        return true;
    }

    private boolean trySell() {
        int melons = 0;
        for(ItemStack item : mc.player.getInventory().main) {
            if(item.getItem() == Items.MELON) melons++;
        }

        if(melons < 15) return false;

        ChatModuleUtils.sendMessage("/sellall melon", false);
        return true;
    }

    private boolean tryBuy() {
        int openSpace = 0;
        for(ItemStack item : mc.player.getInventory().main) {
            if(item.getItem() == Items.AIR) openSpace++;
        }

        if(openSpace == 0) {
            mc.setScreen(null);
            return true;
        }
        if(mc.interactionManager == null) return false;

        if(mc.currentScreen instanceof GameMenuScreen) mc.setScreen(null);
        if(mc.currentScreen == null) ChatModuleUtils.sendMessage("/shop Farming", false);
        else if(mc.currentScreen instanceof GenericContainerScreen screen) {
            InventoryClickHelper inv = new InventoryClickHelper(screen);
            if (inv.getTitle().contains("FARMING")) {
                inv.clickSlot(6, 0, SlotActionType.PICKUP);
            } else if (inv.getTitle().contains("Melon Slice")) {
                if(inv.getTitle().contains("stacks")) {
                    if (screen.getScreenHandler().getSlot(22).getStack().getCount() < openSpace)
                        inv.clickSlot(25, 0, SlotActionType.PICKUP);
                    else
                        inv.clickSlot(13, 0, SlotActionType.PICKUP);
                } else {
                    inv.clickSlot(31, 0, SlotActionType.PICKUP);
                }
            }
        }
        return false;
    }
}
