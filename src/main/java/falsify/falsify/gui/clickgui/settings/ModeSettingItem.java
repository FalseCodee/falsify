package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.module.settings.ModeSetting;
import net.minecraft.client.util.math.MatrixStack;

public class ModeSettingItem extends SettingItem<ModeSetting>{
    public ModeSettingItem(ModeSetting setting, double x, double y, double width, double height) {
        super(setting, x, y, width, height);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            setting.cycle();
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawSmoothRect(backgroundColor, matrices, (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
        drawCenteredText(matrices, Falsify.mc.textRenderer, setting.getName() + ": " + setting.getMode(), (int)(x + width/2), (int)(y + height/2 - Falsify.mc.textRenderer.fontHeight/2), textColor.getRGB());
    }
}
