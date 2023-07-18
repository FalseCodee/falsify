package falsify.falsify.gui.editor.module;

import falsify.falsify.Falsify;
import org.joml.Vector2d;

public enum Anchor {
    TOP_LEFT((relative -> relative),
            absolute -> absolute),
    CENTER_LEFT(relative -> new Vector2d(relative.x, relative.y + Falsify.mc.getWindow().getScaledHeight()/2f),
            absolute -> new Vector2d(absolute.x, absolute.y - Falsify.mc.getWindow().getScaledHeight()/2f)),
    BOTTOM_LEFT(relative -> new Vector2d(relative.x, relative.y + Falsify.mc.getWindow().getScaledHeight()),
            absolute -> new Vector2d(absolute.x, absolute.y - Falsify.mc.getWindow().getScaledHeight())),

    TOP_RIGHT(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth() + relative.x, relative.y),
            absolute -> new Vector2d(absolute.x - Falsify.mc.getWindow().getScaledWidth(), absolute.y)),
    CENTER_RIGHT(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth() + relative.x, relative.y + Falsify.mc.getWindow().getScaledHeight()/2f),
            absolute -> new Vector2d(absolute.x - Falsify.mc.getWindow().getScaledWidth(), absolute.y - Falsify.mc.getWindow().getScaledHeight()/2f)),
    BOTTOM_RIGHT(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth() + relative.x, relative.y + Falsify.mc.getWindow().getScaledHeight()),
            absolute -> new Vector2d(absolute.x - Falsify.mc.getWindow().getScaledWidth(), absolute.y - Falsify.mc.getWindow().getScaledHeight())),
    TOP_CENTER(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth()/2f + relative.x, relative.y),
    absolute -> new Vector2d(absolute.x-Falsify.mc.getWindow().getScaledWidth()/2f, absolute.y)),
    CENTER_CENTER(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth()/2f + relative.x, Falsify.mc.getWindow().getScaledHeight()/2f + relative.y),
            absolute -> new Vector2d(absolute.x-Falsify.mc.getWindow().getScaledWidth()/2f, absolute.y - Falsify.mc.getWindow().getScaledHeight()/2f)),
    BOTTOM_CENTER(relative -> new Vector2d(Falsify.mc.getWindow().getScaledWidth()/2f + relative.x, Falsify.mc.getWindow().getScaledHeight() + relative.y),
            absolute -> new Vector2d(absolute.x-Falsify.mc.getWindow().getScaledWidth()/2f, absolute.y - Falsify.mc.getWindow().getScaledHeight()));

    private final AnchorFunction relToAbs;
    private final AnchorFunction absToRel;
    Anchor(AnchorFunction relToAbs, AnchorFunction absToRel) {
        this.relToAbs = relToAbs;
        this.absToRel = absToRel;
    }

    public Vector2d getAbsolutePos(double x, double y) {
        return relToAbs.output(new Vector2d(x, y));
    }

    public Vector2d getRelativePos(double x, double y) {
        return absToRel.output(new Vector2d(x, y));
    }

    @FunctionalInterface
    public interface AnchorFunction {
        Vector2d output(Vector2d input);
    }
}
