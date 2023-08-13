package falsify.falsify.gui.modmenu.primitives.tabs;

import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import net.minecraft.client.gui.DrawContext;

public class CosmeticsTab extends PanelTab {
    public CosmeticsTab(Panel panel) {
        super(panel);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public String getName() {
        return "Cosmetics";
    }
}
