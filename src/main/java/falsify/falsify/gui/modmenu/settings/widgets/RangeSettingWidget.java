package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.SliderWidget;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

public class RangeSettingWidget extends SettingWidget<RangeSetting> implements Draggable {
    private final SliderWidget sliderWidget;
    public RangeSettingWidget(RangeSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 15);
        sliderWidget = new SliderWidget(setting, panel, x + width-width/2-10, y + height / 2 - 5, width/2, 10);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(sliderWidget.handleClick(x, y, button)) {
            this.isActive = true;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());
        RenderUtils.fillXGradient(context, 50, (int) height+1, (int) width, (int) height+2, 0, panel.getTheme().primaryColor().darker().getRGB());

        context.getMatrices().scale(1.2f, 1.2f, 1);
        fr.drawString(context, setting.getName(), 5F, (float) height/2-fr.getStringHeight(setting.getName())*1.2f/2, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().pop();
        sliderWidget.setY(y + height / 2 - 5);
        sliderWidget.setActive(isActive);
        sliderWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        return sliderWidget.onDrag(x, y, button, dx, dy);
    }
}
