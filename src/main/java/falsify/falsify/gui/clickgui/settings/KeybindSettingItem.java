package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Typable;
import falsify.falsify.module.settings.KeybindSetting;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.util.math.MatrixStack;

public class KeybindSettingItem extends SettingItem<KeybindSetting> implements Typable {

    boolean highlighted = false;
    public KeybindSettingItem(KeybindSetting setting, double x, double y, double width, double height) {
        super(setting, x, y, width, height);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            highlighted = !highlighted;
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(!highlighted) {
            drawSmoothRect(backgroundColor, matrices, (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
            drawCenteredTextWithShadow(matrices, Falsify.mc.textRenderer, setting.getName() + ": " + ChatModuleUtils.keyCodeToString(setting.getValue()), (int) (x + width / 2), (int) (y + height / 2 - Falsify.mc.textRenderer.fontHeight / 2), textColor.getRGB());
        } else {
            drawSmoothRect(backgroundColor.brighter(), matrices, (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
            drawCenteredTextWithShadow(matrices, Falsify.mc.textRenderer, "SELECT KEY", (int) (x + width / 2), (int) (y + height / 2 - Falsify.mc.textRenderer.fontHeight / 2), textColor.getRGB());
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(highlighted) {
            setting.setValue(keyCode);
            highlighted = false;
            return true;
        }
        return false;
    }
}
