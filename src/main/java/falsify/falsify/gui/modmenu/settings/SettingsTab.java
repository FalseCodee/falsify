package falsify.falsify.gui.modmenu.settings;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.gui.modmenu.settings.widgets.*;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.*;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;

public class SettingsTab extends PanelTab {
    private final Module module;
    private final ArrayList<SettingWidget<?>> settingWidgets = new ArrayList<>();
    private final Color toggledOffColor = new Color(255, 94, 94);
    private final Color toggledOnColor = new Color(83, 255, 75);
    private final Clickable backButton;
    private final Clickable toggleButton;
    public SettingsTab(Panel panel, Module module) {
        super(panel);
        this.module = module;
        backButton = new Clickable.ButtonBuilder()
                .pos(x+10, y+10)
                .dimensions(60, 20)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1, y1)) {
                        panel.setActiveIndex(0);
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    instance.pushStackToPosition(context.getMatrices());
                    Color color = panel.getTheme().primaryColor().darker();
                    if(instance.isHovering(mouseX, mouseY)) color = color.darker();

                    RenderHelper.drawSmoothRect(color.darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
                    RenderHelper.drawSmoothRect(color, context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
                    Falsify.fontRenderer.drawCenteredString(context, "Back", (float) instance.getWidth()/2, (float) instance.getHeight()/2-6, panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                })
                .build();

        toggleButton = new Clickable.ButtonBuilder()
                .pos(x+width-70, y+10)
                .dimensions(60, 20)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1, y1)) {
                        module.toggle();
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    instance.pushStackToPosition(context.getMatrices());
                    Color color = (module.isEnabled()) ? toggledOnColor : toggledOffColor;
                    if(instance.isHovering(mouseX, mouseY)) color = color.darker();

                    RenderHelper.drawSmoothRect(color.darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
                    RenderHelper.drawSmoothRect(color, context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
                    Falsify.fontRenderer.drawCenteredString(context, (module.isEnabled()) ? "Enabled" : "Disabled", (float) instance.getWidth()/2, (float) instance.getHeight()/2-6, panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                })
                .build();
        setWidgets();
    }

    @Override
    public String getName() {
        return "Settings";
    }

    private void setWidgets() {
        double padding = 3;
        double x = this.x + padding;
        double y = this.y + 50;
        double width = this.width - padding * 2;

        for(Setting<?> setting : module.settings) {
            SettingWidget<?> widget = null;
            if(setting instanceof BooleanSetting) widget = new BooleanSettingWidget((BooleanSetting) setting, panel, x, y, width);
            else if(setting instanceof RangeSetting) widget = new RangeSettingWidget((RangeSetting) setting, panel, x, y, width);
            else if(setting instanceof KeybindSetting) widget = new KeybindSettingWidget((KeybindSetting) setting, panel, x, y, width);
            else if(setting instanceof ModeSetting) widget = new ModeSettingWidget((ModeSetting) setting, panel, x, y, width);
            else if(setting instanceof ColorSetting) widget = new ColorSettingWidget((ColorSetting) setting, panel, x, y, width);

            if(widget != null) {
                settingWidgets.add(widget);
                y += widget.getHeight() + padding;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        panel.drawBackground(context, mouseX, mouseY, delta);
        MatrixStack matrices = context.getMatrices();
        FontRenderer fr = Falsify.fontRenderer;

        pushStackToPosition(context.getMatrices());
        matrices.push();
        matrices.translate(width/2f, 5, 0);
        matrices.scale(1.5f, 1.5f, 1);
        fr.drawCenteredString(context, module.name, 0, 0, panel.getTheme().primaryTextColor(),true);
        matrices.pop();

        fr.drawCenteredString(context, module.description, (float) (width/2f), 25, panel.getTheme().secondaryTextColor(), true);
        context.fill(0, 40, (int) width, 43, panel.getTheme().secondaryColor().getRGB());
        context.fill(0, 41, (int) width, 42, panel.getTheme().secondaryColor().brighter().brighter().getRGB());
        context.getMatrices().pop();
        enableScissor(x+3, y+44, x+width-3, y+height-3);
        settingWidgets.stream().filter(moduleEntry -> moduleEntry.getY() < y + height && moduleEntry.getY() + moduleEntry.getHeight() > y).forEach(settingWidget -> settingWidget.render(context, mouseX, mouseY, delta));
        disableScissor();

        backButton.render(context, mouseX, mouseY, delta);
        toggleButton.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        backButton.handleClick(x, y, button);
        toggleButton.handleClick(x, y, button);

        for(SettingWidget<?> settingWidget : settingWidgets) {
            if(settingWidget.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(!isHovering(mouseX, mouseY)) return false;
        double topClamp = y + 48;
        double bottomClamp = y + height - 15 * 2;
        double topPoint = settingWidgets.get(0).getY();
        double bottomPoint = settingWidgets.get(settingWidgets.size() - 1).getY() + settingWidgets.get(settingWidgets.size() - 1).getHeight();
        if(bottomClamp-topClamp > bottomPoint-topPoint) return true;
        amount *= 10;
        if (settingWidgets.size() == 0) return true;
        double scrollDistance = MathUtils.clamp(amount, bottomClamp - bottomPoint, topClamp - topPoint);
        for (SettingWidget<?> sw : settingWidgets) {
            sw.setY(sw.getY() + scrollDistance);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(SettingWidget<?> settingWidget : settingWidgets) {
            if(settingWidget instanceof Typable typable && typable.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        if(keyCode == 256) panel.setActiveIndex(0);
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        for(SettingWidget<?> settingWidget : settingWidgets) {
            if(settingWidget instanceof Draggable draggable && draggable.onDrag(x, y, button, dx, dy)) return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        settingWidgets.forEach(settingWidget -> settingWidget.setActive(false));
        return super.mouseReleased(x, y, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for(SettingWidget<?> settingWidget : settingWidgets) {
            if(settingWidget.charTyped(chr, modifiers)) return true;
        }
        return false;
    }
}
