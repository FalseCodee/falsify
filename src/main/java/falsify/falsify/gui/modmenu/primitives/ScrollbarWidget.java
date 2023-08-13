package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.utils.MathUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class ScrollbarWidget extends PanelWidget implements Draggable {

    private double topClamp;
    private double bottomClamp;
    private double topPoint;
    private double bottomPoint;
    private double barSize;
    private Consumer<Double> consumer;
    private double dy;

    public ScrollbarWidget(Panel panel, double x, double y, double topClamp, double bottomClamp, double topPoint, double bottomPoint) {
        super(panel, x, y, 5, 10);

        this.topClamp = topClamp;
        this.bottomClamp = bottomClamp;
        this.topPoint = topPoint;
        this.bottomPoint = bottomPoint;

        barSize = (bottomClamp-topClamp) / (bottomPoint-topPoint) * (bottomClamp-topClamp);
        height = barSize;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(barSize > panel.getHeight()-20) return;
        pushStackToPosition(context.getMatrices());
        drawSmoothRect(panel.getTheme().secondaryColor().darker(), context.getMatrices(), -1, -1, (float) width+1, (float) height+1, 2.5f, new int[] {10,10,10,10});
        drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), 0, 0, (float) width, (float) height, 2.5f, new int[] {10,10,10,10});
        context.getMatrices().pop();
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x,y)) {
            dy = y - this.y;
            isActive = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(isActive) {
            double tmp = MathUtils.clamp(y-this.dy, topClamp, bottomClamp-barSize);
            if(consumer != null) consumer.accept(tmp-this.y);
            this.y = tmp;
        }
        return false;
    }

    public void setChangedListener(Consumer<Double> consumer) {
        this.consumer = consumer;
    }

    public double getTopClamp() {
        return topClamp;
    }

    public void setTopClamp(double topClamp) {
        this.topClamp = topClamp;
    }

    public double getBottomClamp() {
        return bottomClamp;
    }

    public void setBottomClamp(double bottomClamp) {
        this.bottomClamp = bottomClamp;
    }

    public double getTopPoint() {
        return topPoint;
    }

    public void setTopPoint(double topPoint) {
        this.topPoint = topPoint;
    }

    public double getBottomPoint() {
        return bottomPoint;
    }

    public void setBottomPoint(double bottomPoint) {
        this.bottomPoint = bottomPoint;
    }

    public double getBarSize() {
        return barSize;
    }

    public void setBarSize(double barSize) {
        this.barSize = barSize;
        this.height = barSize;
    }

    public Consumer<Double> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<Double> consumer) {
        this.consumer = consumer;
    }
}
