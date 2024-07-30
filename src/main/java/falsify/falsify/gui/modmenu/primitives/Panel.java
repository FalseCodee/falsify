package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.tabs.waypoint.WaypointTab;
import falsify.falsify.gui.utils.*;
import falsify.falsify.gui.modmenu.tabs.modlist.ModTab;
import falsify.falsify.gui.modmenu.tabs.automation.AutomationTab;
import falsify.falsify.gui.modmenu.tabs.ThemesTab;
import falsify.falsify.utils.ScissorStack;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class Panel extends Clickable implements Scrollable, Typable, Draggable {

    private final ArrayList<PanelTab> tabs;
    private final TabWidget tabWidget;
    private final Theme theme;
    private final ScissorStack scissorStack = new ScissorStack();
    private PanelTab activeTab;
    private final Animation animation = new Animation(500, Animation.Type.EASE_IN_OUT);
    private int activeIndex;
    public Panel(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.tabs = new ArrayList<>();
        this.theme = Falsify.theme;
        tabs.add(new ModTab(this));
        tabs.add(new ThemesTab(this));
        tabs.add(new AutomationTab(this));
        tabs.add(new WaypointTab(this));
        animation.rise();
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
        animation.tick();
        context.getMatrices().push();
        context.getMatrices().translate(0, animation.interpolate(Falsify.mc.getWindow().getScaledHeight()-this.y, 0), 0);
        if(animation.getState() == Animation.State.LOWERING && animation.getProgress() == 0.0) Falsify.mc.setScreen(null);
        activeTab.render(context, mouseX, mouseY, delta);
        tabWidget.render(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
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
        Falsify.shaderManager.GLOW_OUTLINE.startCapture(true);
        drawSmoothRect(theme.primaryColor(), context.getMatrices(), 0, 0, (float) width, (float) height, 7, new int[] {10, 0, 0, 10});
        Falsify.shaderManager.GLOW_OUTLINE.endCapture();
        Falsify.shaderManager.GLOW_OUTLINE.renderShader();
        context.getMatrices().pop();
    }

    public boolean close() {
        animation.lower();
        return false;
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

    public ScissorStack getScissorStack() {
        return scissorStack;
    }
}
