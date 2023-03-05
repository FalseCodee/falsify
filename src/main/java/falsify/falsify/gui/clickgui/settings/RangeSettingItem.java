package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.util.math.MatrixStack;

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


    private final Color barColor = new Color(75, 75, 75);
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawSmoothRect(backgroundColor, matrices, (float) x, (float) y, (float) (x + width), (float) (y + height),2, new int[] {5,5,5,5});
        double ratio = (setting.getValue()-setting.getMin()) / (setting.getMax()-setting.getMin());
        drawRect(barColor,matrices, (float)x, (float)y, (float)(x + ratio*width), (float)(y + height));
        drawCenteredText(matrices, Falsify.mc.textRenderer, setting.getName() + ": " + setting.getValue(), (int)(x + width/2), (int)(y + height/2 - Falsify.mc.textRenderer.fontHeight/2), textColor.getRGB());
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
