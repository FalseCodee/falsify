package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.module.settings.ModeSetting;
import net.minecraft.client.gui.DrawContext;

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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawSmoothRect(backgroundColor.brighter(), context.getMatrices(), (float) x-1, (float) y-1, (float) (x + width)+1, (float) (y + height)+1,3, new int[] {5,5,5,5});
        drawSmoothRect(backgroundColor, context.getMatrices(), (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, setting.getName() + ": " + setting.getMode(), (int)(x + width/2), (int)(y + height/2 - Falsify.mc.textRenderer.fontHeight/2), textColor.getRGB());
    }
}
