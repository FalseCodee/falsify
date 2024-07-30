package falsify.falsify.gui.modmenu.tabs.modlist;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.gui.modmenu.primitives.ScrollbarWidget;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.ScissorStack;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModTab extends PanelTab {

    private final ArrayList<ModuleEntry> moduleEntries = new ArrayList<ModuleEntry>();
    private List<ModuleEntry> filteredModuleEntries;
    private final double padding = 8;
    private int columns = 0;
    private double entryWidth;
    private final double entryHeight = 50;
    private final FilterWidget filterWidget;
    private final ScrollbarWidget scrollbarWidget;


    public ModTab(Panel panel) {
        super(panel);
        this.filterWidget = new FilterWidget(panel,x-100, y, 100, height, this);
        do {
            columns++;
            this.entryWidth = width / columns - padding * ((double) columns+1)/columns-2f;
        } while (entryWidth > 200);
        loadModuleEntries();
        double topClamp = y + padding;
        double bottomClamp = y + height - padding * 2;
        double topPoint = filteredModuleEntries.get(0).getY();
        double bottomPoint = filteredModuleEntries.get(filteredModuleEntries.size() - 1).getY() + entryHeight;
        scrollbarWidget = new ScrollbarWidget(panel, x+width-10, topClamp, topClamp, bottomClamp, topPoint, bottomPoint);
        scrollbarWidget.setConsumer(aDouble -> {
            for (ModuleEntry me : filteredModuleEntries) {
                me.setY(me.getY() + -1.0 * aDouble / ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            }
        });
    }

    @Override
    public String getName() {
        return "Mods";
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(filterWidget.handleClick(x, y, button)) return true;
        if(scrollbarWidget.handleClick(x, y, button)) return true;
        for(ModuleEntry me : filteredModuleEntries) {
            if(me.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ScissorStack scissorStack = panel.getScissorStack();

        pushStackToPosition(context.getMatrices());
        Falsify.shaderManager.GLOW_OUTLINE.startCapture(true);
        drawSmoothRect(panel.getTheme().primaryColor(), context.getMatrices(), 0, 0, (float) width, (float) height, 7, new int[] {10, 0, 0, 0});
        Falsify.shaderManager.GLOW_OUTLINE.endCapture();
        Falsify.shaderManager.GLOW_OUTLINE.renderShader();


        context.getMatrices().pop();
        scissorStack.push(x, (y+padding), (x+width), (y+height-padding));
        filteredModuleEntries.stream().filter(moduleEntry -> moduleEntry.getY() < y + height && moduleEntry.getY() + moduleEntry.getHeight() > y).forEach(moduleEntry -> moduleEntry.render(context, mouseX, mouseY, delta));
        scissorStack.pop();
        filterWidget.render(context, mouseX, mouseY, delta);
        scrollbarWidget.render(context, mouseX, mouseY, delta);
    }

    public void loadModuleEntries() {
        for(Module module : ModuleManager.modules.stream().filter(module -> module.category != Category.AUTOMATION).toList()) {
            moduleEntries.add(new ModuleEntry(panel, module, panel.getScissorStack(), entryWidth, entryHeight));
        }
        setFilter((moduleEntry -> true));
    }

    private void setEntryPositions() {
        for(int i = 0; i < filteredModuleEntries.size(); i++) {
            ModuleEntry me = filteredModuleEntries.get(i);
            me.setX(x + padding + (padding+entryWidth) * (i%columns));
            me.setWidth(entryWidth);
            me.setY(y + padding + (entryHeight+padding) * (i/columns));
        }
    }

    public void setFilter(Predicate<? super ModuleEntry> predicate) {
        filteredModuleEntries = moduleEntries.stream().filter(predicate).toList();
        setEntryPositions();
        if(scrollbarWidget == null || filteredModuleEntries.size() == 0) return;

        double topClamp = y + padding;
        double bottomClamp = y + height - padding * 2;
        double topPoint = filteredModuleEntries.get(0).getY();
        double bottomPoint = filteredModuleEntries.get(filteredModuleEntries.size() - 1).getY() + entryHeight;
        scrollbarWidget.setY(topClamp);
        scrollbarWidget.setTopClamp(topClamp);
        scrollbarWidget.setBottomClamp(bottomClamp);
        scrollbarWidget.setTopPoint(topPoint);
        scrollbarWidget.setBottomPoint(bottomPoint);
        scrollbarWidget.setBarSize((bottomClamp-topClamp) / (bottomPoint-topPoint) * (bottomClamp-topClamp));

        scrollbarWidget.setConsumer(aDouble -> {
            for (ModuleEntry me : filteredModuleEntries) {
                me.setY(me.getY() + -1.0 * aDouble / ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            }
        });
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(isHovering(mouseX, mouseY)) {
            double topClamp = y + padding;
            double bottomClamp = y + height - padding * 2;
            double topPoint = filteredModuleEntries.get(0).getY();
            double bottomPoint = filteredModuleEntries.get(filteredModuleEntries.size() - 1).getY() + entryHeight;
            if (bottomClamp - topClamp > bottomPoint - topPoint) return true;
            amount *= 10;
            if (filteredModuleEntries.size() == 0) return true;
            double scrollDistance = MathUtils.clamp(amount, bottomClamp - bottomPoint, topClamp - topPoint);
            for (ModuleEntry me : filteredModuleEntries) {
                me.setY(me.getY() + scrollDistance);
            }
            scrollbarWidget.setY(scrollbarWidget.getY() + scrollDistance * -1.0 * ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            return true;
        }
        return filterWidget.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        return scrollbarWidget.onDrag(x, y, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        scrollbarWidget.setActive(false);
        return super.mouseReleased(x, y, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(filterWidget.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return filterWidget.charTyped(chr, modifiers);
    }

}
