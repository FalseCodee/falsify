package falsify.falsify.mixin.special;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventEntityRender;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer<T extends Entity> {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void renderLabel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        EventEntityRender.Label event = new EventEntityRender.Label(matrices, Falsify.mc.getRenderTickCounter().getTickDelta(true), Falsify.mc.gameRenderer.getCamera(), vertexConsumers, entity, text.getString());
        Falsify.onEvent(event);
        if(event.isCancelled()) ci.cancel();
    }
}
