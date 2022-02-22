package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.module.settings.BooleanSetting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class BooleanSettingItem extends SettingItem<BooleanSetting>{
    public BooleanSettingItem(BooleanSetting setting, double x, double y, double width, double height) {
        super(setting, x, y, width, height);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            setting.toggle();
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(backgroundColor,matrices, (float)x, (float)y, (float)(x + width), (float)(y + height));
        drawCenteredText(matrices, Falsify.mc.textRenderer, setting.getName() + ": " + ((setting.getValue()) ? "Enabled" : "Disabled"), (int)(x + width/2), (int)(y + height/2 - Falsify.mc.textRenderer.fontHeight/2), textColor.getRGB());
    }
}
