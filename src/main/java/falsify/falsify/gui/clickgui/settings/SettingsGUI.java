package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.module.settings.Setting;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsGUI extends Screen {

    private final Module module;
    private final List<SettingItem<?>> settingItems = new ArrayList<>();

    public SettingsGUI(Module module) {
        super(Text.of(""));
        this.module = module;
    }

    @Override
    protected void init() {
        settingItems.clear();

        double xPadding = 5;
        double yPadding = 5;
        double xStart = this.width / 4;
        double yStart = this.height / 4 + Falsify.mc.textRenderer.fontHeight + yPadding;
        double columns = 5;
        while(((3*xStart)-xStart - 2 * xPadding) / columns - (columns-1)*xPadding < 100 && columns > 0) {
            columns--;
        }
        double width = ((3*xStart)-xStart - ((columns+1) * xPadding)) / columns;
        double height = 30;
        for (int i = 0; i < module.settings.size(); i++) {
            Setting<?> setting = module.settings.get(i);
            addSettingItem(setting, xStart + xPadding + (width + xPadding) * (i % columns), yStart + yPadding + (height + yPadding) * (int) (i / columns), width, height);
        }
    }

    public void addSettingItem(Setting<?> setting, double x, double y, double width, double height) {
        if (setting instanceof BooleanSetting) {
            settingItems.add(new BooleanSettingItem((BooleanSetting) setting, x, y, width, height));
        } else if (setting instanceof ModeSetting) {
            settingItems.add(new ModeSettingItem((ModeSetting) setting, x, y, width, height));
        } else if (setting instanceof RangeSetting) {
            settingItems.add(new RangeSettingItem((RangeSetting) setting, x, y, width, height));

        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(SettingItem<?> settingItem : settingItems) {
            if(settingItem instanceof Draggable draggable) {
                if(draggable.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(SettingItem<?> settingItem : settingItems) {
            if(settingItem.handleClick(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderHelper.drawSmoothRect(new Color(100, 100, 100), matrices, width/4, height/4, 3*width/4, 3*height/4, 5, new int[] {15,15,15,15});
        RenderHelper.drawRect(new Color(50,50,50, 100), matrices, 0, 0, width, height);
        drawCenteredText(matrices, Falsify.mc.textRenderer, module.name, width/2, height/4 + 5, new Color(255, 255, 255).getRGB());
        for(SettingItem<?> settingItem : settingItems) {
            settingItem.render(matrices, mouseX, mouseY, delta);
        }
    }
}
