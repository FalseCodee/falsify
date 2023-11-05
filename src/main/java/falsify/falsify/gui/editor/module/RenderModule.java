package falsify.falsify.gui.editor.module;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.MathUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2d;

import java.awt.*;

public abstract class RenderModule<T extends DisplayModule<?>> extends Clickable implements Draggable {
    protected T module;
    private Anchor anchor;
    protected boolean dragging = false;
    protected double scale = 1;
    protected double dh = 0;
    protected double dw = 0;

    protected double relativeX;
    protected double relativeY;
    private final ScaleModule scaleModule;
    private final Snapper snapper;


    public RenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
        scaleModule = new ScaleModule(this, x + width-5+2.5*scale, y + height-2.5,5, 5);
        anchor = getClosestAnchor();
        Vector2d rel = anchor.getRelativePos(this.x, this.y);
        relativeX = rel.x;
        relativeY = rel.y;
        snapper = new Snapper(this);
    }

    private Anchor getClosestAnchor() {
        Vector2d middle = getMiddle();
        float thirdWidth = Falsify.mc.getWindow().getScaledWidth()/3f;
        float thirdHeight = Falsify.mc.getWindow().getScaledHeight()/3f;
        String xPos;
        String yPos;
        switch ((int) (middle.x/thirdWidth)) {
            default -> xPos = "LEFT";
            case 1 -> xPos = "CENTER";
            case 2 -> xPos = "RIGHT";
        }
        switch ((int) (middle.y/thirdHeight)) {
            default -> yPos = "TOP";
            case 1 -> yPos = "CENTER";
            case 2 -> yPos = "BOTTOM";
        }
        return Anchor.valueOf(yPos + "_" + xPos);
    }

    public RenderModule(double x, double y, double width, double height, T module) {
        this(x, y, width, height);
        this.module = module;
    }

    public void fixLocation() {
        if(x < 0) x = 0;
        else if(y < 0) y = 0;
        else if(x+width*scale > Falsify.mc.getWindow().getScaledWidth()) x = Falsify.mc.getWindow().getScaledWidth()-width*scale;
        else if(y+height*scale > Falsify.mc.getWindow().getScaledHeight()) y = Falsify.mc.getWindow().getScaledHeight()-height*scale;
        else return;
        anchor = getClosestAnchor();
        Vector2d rel = anchor.getRelativePos(this.x, this.y);
        relativeX = rel.x;
        relativeY = rel.y;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        double sf = getScaleFactor();

        if(scaleModule.handleClick(x, y, button)) return true;
        if(isHovering(x, y)){
            if(!dragging) {
                dragging = true;
                dw = (this.x - x);
                dh = (this.y - y);
            }
            return true;
        }
        return false;
    }

    @Override
    public Vector2d getMiddle() {
        return new Vector2d(x + width*scale/2, y + height*scale/2);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Vector2d abs = anchor.getAbsolutePos(relativeX, relativeY);
        this.x = abs.x;
        this.y = abs.y;
        fixLocation();
        if(isDragging()) {
            if(snapper.isSnappingX()) horizontalLine(context.getMatrices(), (float) Snapper.currentSnapX, 0.5f, Color.WHITE);
            if(snapper.isSnappingY()) verticalLine(context.getMatrices(), (float) Snapper.currentSnapY, 0.5f, Color.WHITE);
        }
        context.getMatrices().push();
        context.getMatrices().translate(x,y,0);
        context.getMatrices().scale((float) scale, (float) scale, 1);

        renderModule(context, mouseX, mouseY, delta);
        if(Falsify.mc.currentScreen instanceof EditGUI) {
            context.drawVerticalLine(0, 0, (int) (height), Color.WHITE.getRGB());
            context.drawVerticalLine((int) width, 0, (int) height, Color.WHITE.getRGB());
            context.drawHorizontalLine(0, (int) width, 0, Color.WHITE.getRGB());
            context.drawHorizontalLine(0, (int) width, (int) (height), Color.WHITE.getRGB());
            TextRenderer tr = Falsify.mc.textRenderer;
            Vector2d center = getMiddle();
            context.drawText(tr, module.name, (center.x*2 <= WINDOW.getScaledWidth()) ? 2 : (int) (width - tr.getWidth(module.name)-2), (center.y*2 > WINDOW.getScaledHeight()) ? -tr.fontHeight : (int) height+4, -1, true);
        }
        scaleModule.setX(this.x + (this.width-5+2.5)*scale);
        scaleModule.setY(this.y + (this.height-5+2.5)*scale);

        if(Falsify.mc.currentScreen != null && Falsify.mc.currentScreen.getClass() == EditGUI.class){
            context.getMatrices().translate((this.width-5+2.5), (this.height-5+2.5), 0);
            scaleModule.render(context, mouseX, mouseY, delta);
        }
        context.getMatrices().pop();

    }

    public abstract void renderModule(DrawContext context, int mouseX, int mouseY, float delta);

    @Override
    public boolean isHovering(double x, double y) {
        return x >= getX() && y >= getY() && x <= getX() + getWidth()*scale && y <= getY() + getHeight()*scale;
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

    public Anchor getAnchor() {
        return anchor;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public void setRelativeX(double relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(double relativeY) {
        this.relativeY = relativeY;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(scaleModule.onDrag(x, y, button, dx, dy)) return true;

        if(dragging) {
            double[] pos = snapToGrid(x + dw, y + dh);
            this.x = pos[0];
            this.y = pos[1];
            this.dx = dx;
            this.dy = dy;
            anchor = getClosestAnchor();
            Vector2d rel = anchor.getRelativePos(this.x, this.y);
            relativeX = rel.x;
            relativeY = rel.y;
            snapper.update();
            return true;
        }
        return false;
    }

    public boolean horizontalSnap(Snapper snapper) {
        return this.snapper.horizontalSnap(snapper);
    }

    public boolean verticalSnap(Snapper snapper) {
        return this.snapper.verticalSnap(snapper);
    }

    public Snapper getSnapper() {
        return this.snapper;
    }

    public double getScaledWidth() {
        return width*scale;
    }
    public double getScaledHeight() {
        return height*scale;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        scale = MathUtils.clamp((float) scale, 0.1f, 3);
        this.scale = scale;
    }

    public double[] snapToGrid(double x, double y) {
        return new double[] {x, y};

    }

    @Override
    public void setWidth(double width) {
        if(anchor == Anchor.TOP_RIGHT || anchor == Anchor.CENTER_RIGHT || anchor == Anchor.BOTTOM_RIGHT) relativeX += (this.width-width)*scale;
        else if(anchor == Anchor.TOP_CENTER || anchor == Anchor.CENTER_CENTER || anchor == Anchor.BOTTOM_CENTER) relativeX += (this.width-width)*scale/2.0;
        super.setWidth(width);
    }

    @Override
    public void setHeight(double height) {
        if(anchor == Anchor.BOTTOM_LEFT || anchor == Anchor.BOTTOM_CENTER || anchor == Anchor.BOTTOM_RIGHT) relativeY += (this.height-height)*scale;
        else if(anchor == Anchor.CENTER_LEFT || anchor == Anchor.CENTER_CENTER || anchor == Anchor.CENTER_RIGHT) relativeY += (this.height-height)*scale/2.0;
        super.setHeight(height);
    }
}
