package falsify.falsify.cosmetics.cosmetics;

import falsify.falsify.Falsify;
import falsify.falsify.cosmetics.Cosmetic;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class MaskCosmetic extends Cosmetic {
    public MaskCosmetic() {
        super(ModelPartBuilder.create().uv(0, 0)
                .cuboid(-4, -1.3f, -4, 8, 8, 8, Dilation.NONE.add(0.6f)), new LegacyIdentifier("mask", 400, 200).getId(), 32, 16);
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
    protected VertexConsumer fetchVertexConsumer(VertexConsumerProvider provider, Identifier texture) {
        return provider.getBuffer(RenderLayer.getEntityTranslucent(texture));
    }
}
