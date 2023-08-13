package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.gui.utils.Clickable;

public abstract class PanelWidget extends Clickable {
    protected final Panel panel;
    protected boolean isActive = false;
    public PanelWidget(Panel panel, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.panel = panel;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        return false;
    }
    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public Panel getPanel() {
        return panel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
