package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class RangeSettingItem extends SettingItem<RangeSetting> implements Draggable {
    public RangeSettingItem(RangeSetting setting, double x, double y, double width, double height) {
        super(setting, x, y, width, height);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            if(!isActive()) {
                setActive(true);
            }
            return true;
        }
        return false;
    }


    private final Color barColor = new Color(183, 183, 183);
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawSmoothRect(backgroundColor.brighter(), context.getMatrices(), (float) x-1, (float) y-1, (float) (x + width)+1, (float) (y + height)+1,3, new int[] {5,5,5,5});
        drawSmoothRect(backgroundColor, context.getMatrices(), (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
        double ratio = (setting.getValue()-setting.getMin()) / (setting.getMax()-setting.getMin());
        drawRect(barColor,context.getMatrices(), (float)x, (float)y, (float)(x + ratio*width), (float)(y + height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, setting.getName() + ": " + setting.getValue(), (int)(x + width/2), (int)(y + height/2 - Falsify.mc.textRenderer.fontHeight/2), textColor.getRGB());
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(isActive()) {

            double mouseRatio = (x - this.x) / this.width;
            double mappedValue = setting.getMin() + (mouseRatio * (setting.getMax() - setting.getMin()));

            setting.setValue(mappedValue);

            return true;
        }
        return false;
    }
}
