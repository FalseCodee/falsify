package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;


public abstract class EventEntityRender extends Event<EventEntityRender> {
    private final MatrixStack matrices;
    private final float tickDelta;
    private final Camera camera;
    private final VertexConsumerProvider vertexConsumerProvider;
    private final Entity entity;

    public EventEntityRender(MatrixStack matrices, float tickDelta, Camera camera, VertexConsumerProvider vertexConsumers, Entity entity) {
        this.tickDelta = tickDelta;
        this.matrices = matrices;
        this.camera = camera;
        this.vertexConsumerProvider = vertexConsumers;
        this.entity = entity;
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

    public static class Model extends EventEntityRender {
        private final EntityModel<?> model;

        public Model(MatrixStack matrices, float tickDelta, Camera camera, VertexConsumerProvider vertexConsumers, Entity entity, EntityModel<?> model) {
            super(matrices, tickDelta, camera, vertexConsumers, entity);
            this.model = model;
        }

        public EntityModel<?> getModel() {
            return model;
        }
    }

    public static class Label extends EventEntityRender {
        private final String text;
        public Label(MatrixStack matrices, float tickDelta, Camera camera, VertexConsumerProvider vertexConsumers, Entity entity, String text) {
            super(matrices, tickDelta, camera, vertexConsumers, entity);
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}