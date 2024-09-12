package falsify.falsify.cosmetics.cosmetics;

import falsify.falsify.Falsify;
import falsify.falsify.cosmetics.Cosmetic;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;

public class HaloCosmetic extends Cosmetic {
    public HaloCosmetic() {
        super(ModelPartBuilder.create().uv(0, 0)
                .cuboid(-5, -1, -5, 10, 1.2f, 1)
                .cuboid(-5, -1, 4, 10, 1.2f, 1)
                .cuboid(-5, -1, -4, 1, 1.2f, 8)
                .cuboid(4, -1, -4, 1, 1.2f, 8), new LegacyIdentifier("noise", 16, 16).getId());
    }

    private float rotation = 0.0f;

    @Override
    protected void applyTranslations(LivingEntity livingEntity, BipedEntityModel<?> model, MatrixStack matrixStack) {
        rotation += (float) (1.75 * Falsify.mc.getRenderTickCounter().getLastFrameDuration());

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrixStack.translate(0, -0.7f + Math.sin(rotation / 50) / 10, 0);
    }

    @Override
    protected void renderPart(MatrixStack matrixStack, VertexConsumer consumer, int lightLevel, int overlay) {
        getModelPart().render(matrixStack, consumer, lightLevel, overlay, 0xFFFF66);
    }
}
