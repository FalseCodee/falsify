package falsify.falsify.gui.modmenu;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class ModMenuScreen extends Screen {
    private Panel panel;
    private Clickable editHud;
    public ModMenuScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
//        Falsify.theme = new Theme(new Color(177, 203, 255), new Color(0, 186, 210), new Color(225, 225, 225), new Color(0, 167, 176));
        panel = new Panel(width/2f-3*width/10f,height/2f-3*height/10f, 3*width/5f,3*height/5f);
        editHud = new Clickable.ButtonBuilder()
                .pos(width-100, 10)
                .dimensions(90, 40)
                .onClick((instance, x, y, button) -> {
                    if(instance.isHovering(x, y)) {
                        Falsify.mc.setScreen(new EditGUI(this));
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    Color color = panel.getTheme().primaryColor();
                    if(instance.isHovering(mouseX, mouseY)) color = color.brighter();

                    instance.pushStackToPosition(context.getMatrices());
                    RenderHelper.drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 5, new int[] {10, 10, 10, 10});
                    RenderHelper.drawSmoothRect(color, context.getMatrices(), 2, 2, (float) instance.getWidth()-2, (float) instance.getHeight()-2, 4, new int[] {10, 10, 10, 10});

                    Falsify.fontRenderer.drawCenteredString(context, "Edit Hud", (float) (instance.getWidth()/2f), (float) (instance.getHeight()/2f - Falsify.fontRenderer.getStringHeight("Edit Hud")/2f), panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                })
                .build();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(panel.handleClick(mouseX, mouseY, button)) return true;
        return editHud.handleClick(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return panel.mouseScrolled(mouseX, mouseY, verticalAmount);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return panel.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        panel.render(context, mouseX, mouseY, delta);
        editHud.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return panel.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return panel.onDrag(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return panel.charTyped(chr, modifiers);
    }

    @Override
    public void close() {
        if(panel.close()) super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public Panel getPanel() {
        return panel;
    }
}
