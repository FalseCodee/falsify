package falsify.falsify.mixin;

import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.render.ESP;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void EntityRenderPre(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        ESP esp = ModuleManager.getModule(ESP.class);
        if(esp != null && esp.isEnabled() && esp.isGlow() && esp.isValid(entity)) {
            Color color = esp.getColor();
            bufferBuilders.getOutlineVertexConsumers().setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }
}
