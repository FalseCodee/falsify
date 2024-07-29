package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.utils.ColorUtils;

import java.awt.*;

public class ColorSetting extends Setting<Color> {
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean rainbow = false;
    private float rpm = 15;
    public ColorSetting(String name, Color value) {
        super(name);
        hue = ColorUtils.getHue(value);
        saturation = ColorUtils.getSaturation(value);
        brightness = ColorUtils.getBrightness(value);
        alpha = value.getAlpha() / 255.0f;
    }

    @Override
    public Color getValue() {
        return ColorUtils.setAlpha(getColorWithoutAlpha(), alpha);
    }

    public Color getColorWithoutAlpha() {
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public void tick() {
        if(rainbow) hue += rpm / 1000 * Falsify.mc.getRenderTickCounter().getLastFrameDuration();
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public float getRpm() {
        return rpm;
    }

    public void setRpm(float rpm) {
        this.rpm = rpm;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public ColorSetting(String name, int r, int g, int b, int a) {
        this(name, new Color(r,g,b,a));
    }
}
