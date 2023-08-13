package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.utils.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

public class ColorWheelWidget extends PanelWidget implements Draggable {
    private State state;
    private final float radius;
    private final ColorSetting setting;

    public ColorWheelWidget(Panel panel, ColorSetting setting, double x, double y, float radius) {
        super(panel, x-radius, y-radius, radius*2 + 40, radius*2);
        this.radius = radius;
        this.setting = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color currentColor = setting.getColorWithoutAlpha();
        pushStackToPosition(context.getMatrices());
        context.getMatrices().translate(radius, radius, 0);
        drawRainbowCircle(context.getMatrices(), 0, 0, radius, 100, 1.0f);

        context.getMatrices().push();
        context.getMatrices().translate(50, 0, 0);
        context.fillGradient(0, (int) -radius, 10, (int) radius, Color.HSBtoRGB(setting.getHue(), setting.getSaturation(), 1.0f), 0xff000000);
        context.getMatrices().translate(0, radius - (radius*2) * setting.getBrightness(), 0);
        context.fill(-1, -3, 11, 3, currentColor.darker().getRGB());
        context.fill(0, -2, 10, 2, currentColor.getRGB());
        context.getMatrices().pop();

        context.getMatrices().push();
        context.getMatrices().translate(70, 0, 0);
        context.fillGradient(0, (int) -radius, 10, (int) radius, currentColor.getRGB(), 0x00000000);
        context.getMatrices().translate(0, radius - (radius*2) * setting.getAlpha(), 0);
        context.fill(-1, -3, 11, 3, currentColor.darker().getRGB());
        context.fill(0, -2, 10, 2, currentColor.getRGB());
        context.getMatrices().pop();

        Vec2f point = toPoint(setting.getHue(), setting.getSaturation());
        context.getMatrices().translate(point.x, point.y, 0);

        drawSmoothRect(currentColor.darker(), context.getMatrices(), -4.0f, -4.0f, 4.0f, 4.0f, 4.0f, new int[] {10, 10, 10, 10});
        drawSmoothRect(currentColor, context.getMatrices(), -3.0f, -3.0f, 3.0f, 3.0f, 3.0f, new int[] {10, 10, 10, 10});

        context.getMatrices().pop();
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if (isHovering(x, y)) {
            x -= this.x+radius;
            y -= this.y+radius;
            float[] hs = colorFromPoint((int) x, (int) y);
            if (hs != null) {
                state = State.COLOR_WHEEL;
                isActive = true;

                setting.setHue(hs[0]);
                setting.setSaturation(hs[1]);
            } else if (x >= radius+5 && x <= radius+15 && y >= -radius && y <= radius) {
                state = State.BRIGHTNESS;
                isActive = true;

                setting.setBrightness((float) MathUtils.clamp(1 - MathUtils.inverseLerp(-radius, radius, y), 0.0, 1.0));
            } else if (x-20 >= radius+5 && x-20 <= radius+15 && y >= -radius && y <= radius) {
                state = State.ALPHA;
                isActive = true;

                setting.setAlpha((float) MathUtils.clamp(1 - MathUtils.inverseLerp(-radius, radius, y), 0.0, 1.0));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(isActive) {
            if(state == State.COLOR_WHEEL) {
                x -= this.x+radius;
                y -= this.y+radius;
                float[] hs = colorFromPoint((int) x, (int) y);
                assert hs != null;
                setting.setHue(hs[0]);
                setting.setSaturation(hs[1]);
            } else if (state == State.BRIGHTNESS) {
                setting.setBrightness((float) MathUtils.clamp(1-MathUtils.inverseLerp(this.y, this.y + this.height, y), 0.0, 1.0));
            } else if (state == State.ALPHA) {
                setting.setAlpha((float) MathUtils.clamp(1-MathUtils.inverseLerp(this.y, this.y + this.height, y), 0.0, 1.0));
            }
            return true;
        }
        return false;
    }

    private float[] colorFromPoint(int x, int y) {
        float saturation = (float) (Math.sqrt(x*x + y*y) / radius);
        if(saturation == 0.0f) return new float[] {0.0f, 0.0f};

        float hue = (float) ((-Math.atan((float)y / x) + Math.PI / 2) / (Math.PI)) / 2;
        if(x < 0) hue += 0.5f;

        if(!isActive && saturation > 1.0f) return null;
        return new float[] {MathUtils.clamp(hue, 0.0f, 1.0f), MathUtils.clamp(saturation, 0.0f, 1.0f)};
    }

    private Vec2f toPoint(float hue, float saturation) {
        float TWO_PI = (float) (2 * Math.PI);
        hue *= TWO_PI;

        float x = (float) (Math.sin(hue) * saturation) * radius;
        float y = (float) (Math.cos(hue) * saturation) * radius;
        return new Vec2f(x, y);
    }
    private enum State {
        COLOR_WHEEL,
        BRIGHTNESS,
        ALPHA
    }
}
