package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.ScissorStack;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModPanel extends Clickable {

    private final ArrayList<ModuleEntry> moduleEntries = new ArrayList<ModuleEntry>();
    private List<ModuleEntry> filteredModuleEntries;
    private final double padding = 8;
    private int columns = 0;
    private double entryWidth;
    private final double entryHeight = 50;
    private final ScissorStack scissorStack = new ScissorStack();


    public ModPanel(double x, double y, double width, double height) {
        super(x, y, width, height);
        do {
            columns++;
            this.entryWidth = width / columns - padding * ((double) columns+1)/columns;
        } while (entryWidth > 200);
        loadModuleEntries();
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        for(ModuleEntry me : filteredModuleEntries) {
            if(me.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());
        context.fill(0,0, (int) width, (int) height, new Color(80, 80, 80).getRGB());
        context.getMatrices().pop();
        scissorStack.push(x, (y+padding), (x+width), (y+height-padding));
        filteredModuleEntries.forEach(moduleEntry -> moduleEntry.render(context, mouseX, mouseY, delta));
        scissorStack.pop();
    }

    public void loadModuleEntries() {
        for(Module module : ModuleManager.modules) {
            moduleEntries.add(new ModuleEntry(module, scissorStack, entryWidth, entryHeight));
        }
        setFilter((moduleEntry -> true));
    }

    private void setEntryPositions() {
        for(int i = 0; i < filteredModuleEntries.size(); i++) {
            ModuleEntry me = filteredModuleEntries.get(i);
            me.setX(x + padding + (padding+entryWidth) * (i%columns));
            me.setWidth(entryWidth);
            me.setY(y + (entryHeight+padding) * (i/columns));
        }
    }

    public void setFilter(Predicate<? super ModuleEntry> predicate) {
        filteredModuleEntries = moduleEntries.stream().filter(predicate).toList();
        setEntryPositions();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        amount *= 10;
        if(filteredModuleEntries.size() == 0 || filteredModuleEntries.get(0).getY()+amount > y+padding || filteredModuleEntries.get(filteredModuleEntries.size()-1).getY()+entryHeight+amount < y+height-padding*2) return false;

        for(ModuleEntry me : filteredModuleEntries) {
            me.setY(me.getY()+amount);
        }
        return true;
    }
}
