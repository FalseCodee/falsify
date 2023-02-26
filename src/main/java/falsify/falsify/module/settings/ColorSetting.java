package falsify.falsify.module.settings;

import java.awt.*;

public class ColorSetting {
    private final RangeSetting red;
    private final RangeSetting green;
    private final RangeSetting blue;
    private final RangeSetting alpha;

    public ColorSetting(String name, int r, int b, int g, int a) {
        this.red = new RangeSetting(name + " R", r, 0, 255, 1);
        this.green = new RangeSetting(name + " G", g, 0, 255, 1);
        this.blue = new RangeSetting(name + " B", b, 0, 255, 1);
        this.alpha = new RangeSetting(name + " A", a, 0, 255, 1);
    }

    public RangeSetting getRed() {
        return red;
    }

    public RangeSetting getGreen() {
        return green;
    }

    public RangeSetting getBlue() {
        return blue;
    }

    public RangeSetting getAlpha() {
        return alpha;
    }

    public Color getColor() {
        return new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue());
    }
}
