package falsify.falsify.gui.clickgui;

import falsify.falsify.gui.clickgui.Renderable;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

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

    public static class ButtonBuilder {
        private double x;
        private double y;
        private double width;
        private double height;
        private ClickableRunnable onClick;
        private RenderableRunnable onRender;
        public ButtonBuilder() {
            onClick = (clickable, x, y, button) -> clickable.isHovering(x, y);
            onRender = (clickable, context, x, y, delta) -> context.fill((int)clickable.x, (int)clickable.y, (int)(clickable.x + clickable.width), (int)(clickable.y + clickable.height), Color.BLACK.getRGB());
        }

        public ButtonBuilder pos(double x, double y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public ButtonBuilder dimensions(double width, double height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ButtonBuilder onClick(ClickableRunnable run) {
            onClick = run;
            return this;
        }

        public ButtonBuilder onRender(RenderableRunnable run) {
            onRender = run;
            return this;
        }

        public Clickable build() {
            return new Clickable(this.x, this.y, this.width, this.height) {
                @Override
                public boolean handleClick(double x, double y, int button) {
                    return onClick.run(this, x, y, button);
                }

                @Override
                public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                    onRender.run(this, context, mouseX, mouseY, delta);
                }
            };
        }

    }

    @FunctionalInterface
    public interface ClickableRunnable{
        boolean run(Clickable clickable, double x, double y, double button);
    }
@FunctionalInterface
    public interface RenderableRunnable{
        void run(Clickable clickable, DrawContext context, int mouseX, int mouseY, float delta);
    }
}