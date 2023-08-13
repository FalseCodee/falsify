package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.gui.modmenu.primitives.*;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ColorSettingWidget extends SettingWidget<ColorSetting> implements Draggable, Typable {

    private final ColorWheelWidget colorWheel;
    private final CheckboxWidget rainbowCheckBox;
    private final SliderWidget rainbowSlider;
    public ColorSettingWidget(ColorSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 120);
        this.colorWheel = new ColorWheelWidget(panel, setting,x+width-100, y+height/2, 45);
        this.rainbowCheckBox = new CheckboxWidget(panel, x+100, y+height-25, 15, 15);
        this.rainbowCheckBox.setChecked(setting.isRainbow());
        this.rainbowCheckBox.setBooleanConsumer(setting::setRainbow);

        this.rainbowSlider = new SliderWidget(new RangeSetting("Speed", setting.getRpm(), 0.0, 100.0, 1.0), panel, x+120, y+height-25, width/4, 10, true);
        this.rainbowSlider.setSliderConsumer(val -> setting.setRpm(val.floatValue()));


    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(colorWheel.handleClick(x, y, button)) {
            this.isActive = true;
            return true;
        }
        if(rainbowCheckBox.handleClick(x, y, button)) return true;
        if(rainbowCheckBox.isChecked() && rainbowSlider.handleClick(x, y, button)) {
            this.isActive = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(colorWheel.onDrag(x, y, button, dx, dy)) return true;
        return rainbowCheckBox.isChecked() && rainbowSlider.onDrag(x, y, button, dx, dy);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color currentColor = setting.getValue();

        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());
        RenderUtils.fillXGradient(context, 50, (int) height+1, (int) width, (int) height+2, 0, panel.getTheme().primaryColor().darker().getRGB());
        context.getMatrices().push();
        context.getMatrices().scale(1.2f, 1.2f, 1);
        fr.drawString(context, setting.getName(), 5, 2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
        context.fill(5, 20, 95, (int) (height-5), currentColor.getRGB());
        context.getMatrices().translate(120, height-rainbowCheckBox.getHeight()-4, 0);
        fr.drawString(context, "Chroma?", 0, 0, panel.getTheme().secondaryTextColor(), true);
        context.getMatrices().pop();

        colorWheel.setY(y+height/2-45);
        colorWheel.setActive(isActive && !rainbowSlider.isActive());
        colorWheel.render(context, mouseX, mouseY, delta);

        rainbowCheckBox.setY(y+height-rainbowCheckBox.getHeight()-5);
        rainbowCheckBox.render(context, mouseX, mouseY, delta);

        if(rainbowCheckBox.isChecked()) {
            rainbowSlider.setY(y + height - rainbowSlider.getHeight() - 25);
            rainbowSlider.setActive(isActive && !colorWheel.isActive());
            rainbowSlider.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        return textBox.keyPressed(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
//        return textBox.charTyped(chr, modifiers);
        return false;
    }
}
