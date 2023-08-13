package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.utils.Scrollable;
import falsify.falsify.gui.utils.Typable;

public abstract class PanelTab extends Clickable implements Scrollable, Typable, Draggable {
    protected final Panel panel;
    public PanelTab(Panel panel) {
        super(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
        this.panel = panel;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 256) {
            Falsify.mc.setScreen(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public abstract String getName();
}
