package falsify.falsify.cosmetics;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class Cosmetic {
    private ModelPart modelPart;
    private Identifier texture;

    public Cosmetic(ModelPartBuilder builder, Identifier texture) {
        setModelPart(builder);
        this.texture = texture;
    }

    public Cosmetic(ModelPartBuilder builder, Identifier texture, int w, int h) {
        setModelPart(builder, w, h);
        this.texture = texture;
    }

    public void render(LivingEntity livingEntity, EntityModel<?> model, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int lightLevel) {
        if (!(livingEntity instanceof AbstractClientPlayerEntity playerEntity)) return;
        VertexConsumer vertexConsumer = fetchVertexConsumer(vertexConsumerProvider, texture);
        int m = LivingEntityRenderer.getOverlay(playerEntity, 0.0f);

        matrixStack.push();
        applyTranslations(livingEntity, model, matrixStack);
        renderPart(matrixStack, vertexConsumer, lightLevel, m);
        matrixStack.pop();
    }

    protected void applyTranslations(LivingEntity livingEntity, EntityModel<?> model, MatrixStack matrixStack) {
    }

    protected void renderPart(MatrixStack matrixStack, VertexConsumer consumer, int lightLevel, int overlay) {
        modelPart.render(matrixStack, consumer, lightLevel, overlay);
    }

    protected VertexConsumer fetchVertexConsumer(VertexConsumerProvider provider, Identifier texture) {
        return provider.getBuffer(RenderLayer.getEntitySolid(texture));
    }

    public ModelPart getModelPart() {
        return modelPart;
    }

    public void setModelPart(ModelPartBuilder modelPartBuilder, int w, int h) {
        List<ModelPart.Cuboid> cuboids = modelPartBuilder.build().stream().map(modelCuboidData -> modelCuboidData.createCuboid(w, h)).toList();
        modelPart = new ModelPart(cuboids, new HashMap<>());
    }

    public void setModelPart(ModelPartBuilder modelPartBuilder) {
        setModelPart(modelPartBuilder, 16, 16);
    }
    public Identifier getTexture() {
        return texture;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }
}
