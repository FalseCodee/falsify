package falsify.falsify.gui.clickgui;

import falsify.falsify.gui.clickgui.Renderable;
import falsify.falsify.utils.RenderHelper;

public abstract class Clickable extends RenderHelper implements Renderable {
    protected double x;
    protected double y;
    protected double dx = 0.0;
    protected double dy = 0.0;
    protected double width;
    protected double height;

    public Clickable(double x, double y, double width, double height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public abstract boolean handleClick(double x, double y, int button);

    public boolean isHovering(double x, double y) {
        return x >= getX() && y >= getY() && x <= getX() + getWidth() && y <= getY() + getHeight();
    }

    public boolean isIntersecting(Clickable clickable) {
        return (isHovering(clickable.getX(), clickable.getY()) || isHovering(clickable.getX(), clickable.getY() + clickable.getHeight()) || isHovering(clickable.getX() + clickable.getWidth(), clickable.getY()) || isHovering(clickable.getX() + clickable.getWidth(), clickable.getY() + clickable.getHeight()));
    }

    public void fixIntersection(Clickable clickable) {
        int i = 0;
        while (isIntersecting(clickable)) {
            if(i > 20) return;
            i++;
            if(clickable.dx == 0.0) clickable.dx = 1;
            if(clickable.dy == 0.0) clickable.dy = 1;
            if(isHovering(clickable.getX(), clickable.getY())) {
                if(isHovering(clickable.getX(), clickable.getY()-clickable.dy)) {
                    clickable.x -= clickable.dx;
                } else {
                    clickable.y -= clickable.dy;
                }
            } else if(isHovering(clickable.getX(), clickable.getY() + clickable.getHeight())) {
                if(isHovering(clickable.getX(), clickable.getY() + clickable.getHeight() -clickable.dy)) {
                    clickable.x -= clickable.dx;
                } else {
                    clickable.y -= clickable.dy;
                }
            } else if(isHovering(clickable.getX() + clickable.getWidth(), clickable.getY())) {
                if(isHovering(clickable.getX() + clickable.getWidth(), clickable.getY()-clickable.dy)) {
                    clickable.x -= clickable.dx;
                } else {
                    clickable.y -= clickable.dy;
                }
            } else {
                if(isHovering(clickable.getX()+ clickable.getWidth(), clickable.getY()+ clickable.getHeight()-clickable.dy)) {
                    clickable.x -= clickable.dx;
                } else {
                    clickable.y -= clickable.dy;
                }
            }
        }
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

    public void setHeight(double height) {
        this.height = height;
    }
}