package falsify.falsify.utils;

import java.awt.*;

public class ColorUtils {
    public static float getHue(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        float min = Math.min(Math.min(red, green), blue);
        float max = Math.max(Math.max(red, green), blue);

        if (min == max) {
            return 0;
        }

        float hue;
        if (max == red) {
            hue = (green - blue) / (max - min);

        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);

        } else {
            hue = 4f + (red - green) / (max - min);
        }

        return hue / 6;
    }

    public static float getSaturation(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        float min = Math.min(Math.min(red, green), blue);
        float max = Math.max(Math.max(red, green), blue);

        if (max == 0) {
            return 0;
        }

        return (max-min)/max;
    }

    public static float getBrightness(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return Math.max(Math.max(red, green), blue) / 255f;
    }

    public static Color setAlpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha*255));
    }
}
