package falsify.falsify.gui.clickgui;

import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawableHelper;

public abstract class Clickable extends RenderHelper implements Renderable {
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Clickable(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract boolean handleClick(double x, double y, int button);

    public boolean isHovering(double x, double y) {
        return x >= this.x && y >= this.y && x <= this. x + this.width && y <= this.y + this.height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
