package falsify.falsify.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;

import java.util.ArrayList;

public class ScissorStack {
    private final ArrayList<ScissorBox> scissorStack = new ArrayList<>();

    public void push(double x1, double y1, double x2, double y2) {
        scissorStack.add(0, new ScissorBox(x1, y1, x2, y2));
        enable();
    }

    public void pop() {
        scissorStack.remove(0);
        enable();
    }

    public void enable() {
        if(scissorStack.size() == 0) {
            RenderSystem.disableScissor();
            return;
        }
        scissorStack.get(0).enableScissor();
    }

    private record ScissorBox(double x1, double y1, double x2, double y2) {
        public void enableScissor() {
            RenderSystem.enableScissor((int) (x1 * Falsify.mc.getWindow().getScaleFactor()), (int) ((Falsify.mc.getWindow().getScaledHeight() - y2) * Falsify.mc.getWindow().getScaleFactor()), (int) ((x2-x1) * Falsify.mc.getWindow().getScaleFactor()), (int) ((y2-y1) * Falsify.mc.getWindow().getScaleFactor()));
        }
    }
}
