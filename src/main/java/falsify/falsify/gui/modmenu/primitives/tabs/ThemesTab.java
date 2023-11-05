package falsify.falsify.gui.modmenu.primitives.tabs;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.ColorWheelWidget;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.gui.modmenu.primitives.PanelWidget;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ThemesTab extends PanelTab {
    private final Clickable editTheme;
    public ThemesTab(Panel panel) {
        super(panel);
        editTheme = new Clickable.ButtonBuilder()
                .pos(panel.getX() + panel.getWidth() - 65, panel.getY() + panel.getWidth() - 5)
                .dimensions(60, 20)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1, y1)) {
                        Falsify.logger.info("wow button!");
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    instance.pushStackToPosition(context.getMatrices());
                    context.fill(0, 0, (int) instance.getWidth(), (int) instance.getHeight(), panel.getTheme().secondaryColor().getRGB());
                    context.getMatrices().pop();
                }).build();
    }
    @Override
    public boolean handleClick(double x, double y, int button) {
        return editTheme.handleClick(x, y, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        panel.drawBackground(context, mouseX, mouseY, delta);
        editTheme.render(context, mouseX, mouseY, delta);
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
