package falsify.falsify.cosmetics.cosmetics;

import falsify.falsify.Falsify;
import falsify.falsify.cosmetics.Cosmetic;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CrownCosmetic extends Cosmetic {
    public CrownCosmetic() {
        super(ModelPartBuilder.create().uv(0, 0)
                .cuboid(-5, -1.3f, -5, 10, 2f, 1)
                .cuboid(-5, -1.3f, 4, 10, 2f, 1)
                .cuboid(-5, -1.3f, -4, 1, 2f, 8)
                .cuboid(4, -1.3f, -4, 1, 2f, 8)
                //SPIKES ON THE CROWN
                .cuboid(4, -2.7f, -5, 1, 1.5f, 2)
                .cuboid(4, -2.7f, -2, 1, 1.5f, 2)
                .cuboid(4, -2.7f, 1, 1, 1.5f, 2)

                .cuboid(-5, -2.7f, -3, 1, 1.5f, 2)
                .cuboid(-5, -2.7f, 0, 1, 1.5f, 2)
                .cuboid(-5, -2.7f, 3, 1, 1.5f, 2)

                .cuboid(-5, -2.7f, -5, 2, 1.5f, 1)
                .cuboid(-2, -2.7f, -5, 2, 1.5f, 1)
                .cuboid(1, -2.7f, -5, 2, 1.5f, 1)

                .cuboid(-3, -2.7f, 4, 2, 1.5f, 1)
                .cuboid(-0, -2.7f, 4, 2, 1.5f, 1)
                .cuboid(3, -2.7f, 4, 2, 1.5f, 1), new LegacyIdentifier("noise", 16, 16).getId());
    }


    @Override
    protected void applyTranslations(LivingEntity livingEntity, EntityModel<?> model, MatrixStack matrixStack) {
        float tickDelta = Falsify.mc.getRenderTickCounter().getLastFrameDuration();
        float headYaw = MathHelper.lerp(tickDelta, livingEntity.prevHeadYaw, livingEntity.headYaw) - MathHelper.lerp(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
        float headPitch = MathHelper.lerp(tickDelta, livingEntity.prevPitch, livingEntity.getPitch());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(headPitch));
        matrixStack.translate(0,  -0.4, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-headPitch));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-headYaw));

        if (model instanceof BipedEntityModel<?> bipedEntityModel) {
            getModelPart().copyTransform(bipedEntityModel.head);
        }

    }

    @Override
    protected void renderPart(MatrixStack matrixStack, VertexConsumer consumer, int lightLevel, int overlay) {
        getModelPart().render(matrixStack, consumer, lightLevel, overlay, 0xFFD800);
    }
}
