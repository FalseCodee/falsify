package falsify.falsify.gui.editor.module;

import falsify.falsify.utils.RenderHelper;
import org.joml.Vector2d;

import java.awt.*;

public class Snapper {
    private final double[] xSnaps;
    private final double[] ySnaps;
    private static final double SNAPPING_DISTANCE = 10.0;

    public static double currentSnapX;
    public static double currentSnapY;
    private RenderModule<?> rm;

    public Snapper(RenderModule<?> rm) {
        this.rm = rm;
        xSnaps = new double[3];
        ySnaps = new double[3];
        update();
    }

    public Snapper() {
        xSnaps = new double[1];
        ySnaps = new double[1];
    }

    public void update() {
        Vector2d center = rm.getMiddle();
        xSnaps[0] = rm.getX();
        xSnaps[1] = center.x;
        xSnaps[2] = rm.getX() + rm.getScaledWidth();

        ySnaps[0] = rm.getY();
        ySnaps[1] = center.y;
        ySnaps[2] = rm.getY() + rm.getScaledHeight();
    }

    public void update(double x, double y) {
        xSnaps[0] = x;
        ySnaps[0] = y;
    }

    public boolean horizontalSnap(Snapper snapper) {
        for(double xPos : xSnaps) {
            for(double xPosOther : snapper.xSnaps) {
                double dist = xPos - xPosOther;
                if(Math.abs(dist) < SNAPPING_DISTANCE) {
                    rm.setRelativeX(rm.getRelativeX() - dist);
                    currentSnapX = xPos-dist;
                    return true;
                }
            }
        }
        currentSnapX = -1;
        return false;
    }

    public boolean verticalSnap(Snapper snapper) {
        for(double yPos : ySnaps) {
            for(double yPosOther : snapper.ySnaps) {
                double dist = yPos - yPosOther;
                if(Math.abs(dist) < SNAPPING_DISTANCE) {
                    rm.setRelativeY(rm.getRelativeY() - dist);
                    currentSnapY = yPos-dist;
                    return true;
                }
            }
        }
        currentSnapY = -1;
        return false;
    }

    public boolean isSnappingX() {
        return currentSnapX != -1;
    }

    public boolean isSnappingY() {
        return currentSnapY != -1;
    }
}
