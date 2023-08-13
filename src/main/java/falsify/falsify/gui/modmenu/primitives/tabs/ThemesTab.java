package falsify.falsify.gui.modmenu.primitives.tabs;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

public class ThemesTab extends PanelTab {
    public ThemesTab(Panel panel) {
        super(panel);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        panel.drawBackground(context, mouseX, mouseY, delta);
        pushStackToPosition(context.getMatrices());

        context.getMatrices().push();
        context.getMatrices().translate(15, 10, 0);
        context.getMatrices().scale(1.5f, 1.5f, 1.0f);
        fr.drawString(context, "Current Theme", 0, 0, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        //Draw current theme boxes
        context.getMatrices().push();
        float size = (float) ((width-70) / 5);
        context.getMatrices().translate(15, 35, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, size, size, 5.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(panel.getTheme().primaryColor(), context.getMatrices(), 5, 5, size-5, size-20, 3.0f, new int[] {0, 10, 10, 0});
        drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), 5, size-20, size-5, +size-5, 3.0f, new int[] {10, 0, 0, 10});
        fr.drawCenteredString(context, "Icon", size/2, size+2, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().translate(size + 10, 0, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, size, size, 5.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(panel.getTheme().primaryColor(), context.getMatrices(), 5, 5, size-5, size-5, 3.0f, new int[] {10, 10, 10, 10});
        fr.drawCenteredString(context, "Primary", size/2, size+2, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().translate(size + 10, 0, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, size, size, 5.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), 5, 5, size-5, size-5, 3.0f, new int[] {10, 10, 10, 10});
        fr.drawCenteredString(context, "Secondary", size/2, size+2, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().translate(size + 10, 0, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, size, size, 5.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(panel.getTheme().primaryTextColor(), context.getMatrices(), 5, 5, size-5, size-5, 3.0f, new int[] {10, 10, 10, 10});
        fr.drawCenteredString(context, "Primary Text", size/2, size+2, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().translate(size + 10, 0, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, size, size, 5.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(panel.getTheme().secondaryTextColor(), context.getMatrices(), 5, 5, size-5, size-5, 3.0f, new int[] {10, 10, 10, 10});
        fr.drawCenteredString(context, "Secondary Text", size/2, size+2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        context.getMatrices().translate(15, 35 + size + 30, 0);
        context.getMatrices().push();
        context.getMatrices().scale(1.5f, 1.5f, 1.0f);
        fr.drawString(context, "Other Themes", 0, 0, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
        context.getMatrices().pop();

    }

    @Override
    public String getName() {
        return "Themes";
    }
}
