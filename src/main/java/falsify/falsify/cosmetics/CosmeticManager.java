package falsify.falsify.cosmetics;

import falsify.falsify.Falsify;
import falsify.falsify.cosmetics.cosmetics.CrownCosmetic;
import falsify.falsify.cosmetics.cosmetics.HaloCosmetic;
import falsify.falsify.cosmetics.cosmetics.MaskCosmetic;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class CosmeticManager {
    private static final HaloCosmetic halo = new HaloCosmetic();
    private static final CrownCosmetic crown = new CrownCosmetic();
    private static final MaskCosmetic mask = new MaskCosmetic();
    public static void render(LivingEntity livingEntity, BipedEntityModel<?> model, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int lightLevel) {
        if(!(livingEntity instanceof AbstractClientPlayerEntity player)) return;
        if(Falsify.mc.player != null && !player.getUuid().equals(Falsify.mc.player.getUuid())) return;

        mask.render(livingEntity, model, f, tickDelta, matrixStack, vertexConsumerProvider, lightLevel);
        crown.render(livingEntity, model, f, tickDelta, matrixStack, vertexConsumerProvider, lightLevel);
    }
}
