package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;


public class EventEntityRender extends Event<EventEntityRender> {
    private final MatrixStack matrices;
    private final float tickDelta;
    private final Camera camera;
    private final VertexConsumerProvider vertexConsumerProvider;
    private final Entity entity;
    private final Model model;

    public EventEntityRender(MatrixStack matrices, float tickDelta, Camera camera, VertexConsumerProvider vertexConsumers, Entity entity, Model model) {
        this.tickDelta = tickDelta;
        this.matrices = matrices;
        this.camera = camera;
        this.vertexConsumerProvider = vertexConsumers;
        this.entity = entity;
        this.model = model;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public Camera getCamera() {
        return camera;
    }

    public VertexConsumerProvider getVertexConsumerProvider() {
        return vertexConsumerProvider;
    }

    public Entity getEntity() {
        return entity;
    }

    public Model getModel() {
        return model;
    }
}
