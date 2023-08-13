package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.function.Consumer;

public class SliderWidget extends PanelWidget implements Draggable {
    private final RangeSetting setting;
    private final boolean renderTitle;
    private Consumer<Double> sliderConsumer;

    public SliderWidget(RangeSetting setting, Panel panel, double x, double y, double width, double height, boolean renderTitle) {
        super(panel, x, y, width, height);
        this.setting = setting;
        this.renderTitle = renderTitle;
    }
    public SliderWidget(RangeSetting setting, Panel panel, double x, double y, double width, double height) {
        this(setting, panel, x, y, width, height, false);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        double from = this.x+5;
        double to = this.x+width-5;
        if(isHovering(x, y)) {
            double mouseRatio = MathUtils.clamp(MathUtils.inverseLerp(from, to, x), 0.0, 1.0);
            double mappedValue = MathUtils.lerp(setting.getMin(), setting.getMax(), mouseRatio);
            setting.setValue(mappedValue);
            if(sliderConsumer != null) sliderConsumer.accept(mappedValue);

            isActive = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        double from = this.x+5;
        double to = this.x+width-5;
        if(isActive) {
            double mouseRatio = MathUtils.clamp(MathUtils.inverseLerp(from, to, x), 0.0, 1.0);
            double mappedValue = MathUtils.lerp(setting.getMin(), setting.getMax(), mouseRatio);
            setting.setValue(mappedValue);
            if(sliderConsumer != null) sliderConsumer.accept(mappedValue);
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());
        if(renderTitle) fr.drawCenteredString(context, setting.getName(), (float) (width/2f), - 12, panel.getTheme().primaryTextColor(), true);

        context.getMatrices().translate(0, height/4, 0);
        float progress = (float) ((setting.getValue()-setting.getMin()) / (setting.getMax()-setting.getMin()));

        fr.drawString(context, setting.getValue()+"", -fr.getStringWidth(setting.getValue()+"")-5, -4.0f, panel.getTheme().primaryTextColor(), true);

        drawSmoothRect(panel.getTheme().primaryColor().brighter(), context.getMatrices(), (float) ((width-10)*progress), 0, (float) (width-5), 5, 2.5f, new int[] {10,10,10,10});
        drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), 5, 0, (float) (((width-10)*progress) + 5), 5, 2.5f, new int[] {10,10,10,10});

        context.getMatrices().translate(0, 0, 0);
        drawSmoothRect(Color.WHITE.darker(), context.getMatrices(), (float) (((width-10)*progress)-1), -1-2.5f, (float) ((((width-10)*progress) + 10)+1), 7.5f+1, 5.5f, new int[] {10,10,10,10});
        drawSmoothRect(Color.WHITE, context.getMatrices(), (float) ((width-10)*progress), 0-2.5f, (float) (((width-10)*progress) + 10), 7.5f, 5, new int[] {10,10,10,10});

        context.getMatrices().pop();
    }

    public Consumer<Double> getSliderConsumer() {
        return sliderConsumer;
    }

    public void setSliderConsumer(Consumer<Double> sliderConsumer) {
        this.sliderConsumer = sliderConsumer;
    }
}
