package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.util.math.MatrixStack;

public class EventRender3d extends Event<EventRender3d> {
    private final float tickDelta;
    private final MatrixStack matrices;

    public EventRender3d(float tickDelta, MatrixStack matrices) {
        this.tickDelta = tickDelta;
        this.matrices = matrices;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public float getTickDelta() {
        return tickDelta;
    }

}
