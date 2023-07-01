package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class EventRender3d extends Event<EventRender3d> {
    private final float tickDelta;
    private final MatrixStack matrices;
    private final Camera camera;
    private final Matrix4f positionMatrix;

    public EventRender3d(float tickDelta, MatrixStack matrices, Camera camera, Matrix4f positionMatrix) {
        this.tickDelta = tickDelta;
        this.matrices = matrices;
        this.camera = camera;
        this.positionMatrix = positionMatrix;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public Camera getCamera() {
        return camera;
    }

    public Matrix4f getPositionMatrix() {
        return positionMatrix;
    }
}
