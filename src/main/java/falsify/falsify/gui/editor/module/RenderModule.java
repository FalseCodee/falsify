package falsify.falsify.gui.editor.module;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

public abstract class RenderModule<T extends DisplayModule<?>> extends Clickable implements Draggable {
    protected T module;
    public static final int gridSize = 500;
    protected boolean dragging = false;
    protected double scale = 1;
    protected double dh = 0;
    protected double dw = 0;
    private final ScaleModule scaleModule;


    public RenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
        scaleModule = new ScaleModule(this, x + width-5+2.5*scale, y + height-2.5,5, 5);
    }

    public RenderModule(double x, double y, double width, double height, T module) {
        this(x, y, width, height);
        this.module = module;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(scaleModule.handleClick(x, y, button)) return true;
        if(isHovering(x, y)){
            if(!dragging) {
                dragging = true;
                dw = (this.x - x/scale);
                dh = (this.y - y/scale);
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        RenderHelper.convertToScale(context.getMatrices(), scale);
        renderModule(context, mouseX, mouseY, delta);
        scaleModule.setX(this.x + this.width-5+2.5*scale);
        scaleModule.setY(this.y + this.height-5+2.5*scale);
        if(Falsify.mc.currentScreen != null && Falsify.mc.currentScreen.getClass() == EditGUI.class) scaleModule.render(context, mouseX, mouseY, delta);
        context.getMatrices().pop();

    }

    public abstract void renderModule(DrawContext context, int mouseX, int mouseY, float delta);

    @Override
    public boolean isHovering(double x, double y) {
        x /= scale;
        y /= scale;
        return super.isHovering(x, y);
    }

    public T getModule() {
        return module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setScaleDragging(boolean dragging) {
        this.scaleModule.dragging = dragging;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        x /= scale;
        y /= scale;
        dx /= scale;
        dy /= scale;
        if(scaleModule.onDrag(x, y, button, dx, dy)) return true;

        if(dragging) {
            double[] pos = snapToGrid(x + dw, y + dh);
            this.x = pos[0];
            this.y = pos[1];
            this.dx = dx;
            this.dy = dy;
            return true;
        }
        return false;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        scale = MathUtils.clamp((float) scale, 0.1f, 3);

        this.x = this.x * this.scale / scale;
        this.y = this.y * this.scale / scale;
        this.scale = scale;
    }

    public double[] snapToGrid(double x, double y) {
//        return new double[]{Math.round(x / (RenderHelper.getScaleFactor() * Falsify.mc.getWindow().getWidth()) * gridSize) / (0f + gridSize) * RenderHelper.getScaleFactor() * Falsify.mc.getWindow().getWidth()
//                , Math.round(y / (RenderHelper.getScaleFactor() * Falsify.mc.getWindow().getHeight()) * gridSize) / (0f + gridSize) * RenderHelper.getScaleFactor() * Falsify.mc.getWindow().getHeight()
//        };

        return new double[] {x, y};

    }
}
