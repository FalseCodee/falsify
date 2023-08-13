package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.utils.Scrollable;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.gui.modmenu.primitives.modlist.ModPanel;
import falsify.falsify.gui.modmenu.primitives.tabs.CosmeticsTab;
import falsify.falsify.gui.modmenu.primitives.tabs.ThemesTab;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class Panel extends Clickable implements Scrollable, Typable, Draggable {

    private final ArrayList<PanelTab> tabs;
    private final TabWidget tabWidget;
    private final Theme theme;
    private PanelTab activeTab;
    private int activeIndex;
    public Panel(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.tabs = new ArrayList<>();
        this.theme = Falsify.theme;
        tabs.add(new ModPanel(this));
        tabs.add(new ThemesTab(this));
        tabs.add(new CosmeticsTab(this));
        activeIndex = 0;
        activeTab = tabs.get(activeIndex);
        this.tabWidget = new TabWidget(this, tabs,x, y, width, 30);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(activeTab.handleClick(x, y, button)) return true;
        return tabWidget.handleClick(x, y, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        activeTab.render(context, mouseX, mouseY, delta);
        tabWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return activeTab.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return activeTab.mouseReleased(x, y, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return activeTab.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        return activeTab.onDrag(x, y, button, dx, dy);
    }

    public boolean charTyped(char chr, int modifiers) {
        return activeTab.charTyped(chr, modifiers);
    }

    public void drawBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());
        drawSmoothRect(theme.primaryColor(), context.getMatrices(), 0, 0, (float) width, (float) height, 7, new int[] {10, 0, 0, 10});
        context.getMatrices().pop();
    }

    public PanelTab getActiveTab() {
        return activeTab;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveTab(PanelTab activeTab) {
        this.activeTab = activeTab;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setActiveIndex(int index) {
        activeIndex = index;
        activeTab = tabs.get(activeIndex);
    }

    public ArrayList<PanelTab> getTabs() {
        return tabs;
    }
}
