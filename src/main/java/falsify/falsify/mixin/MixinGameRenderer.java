package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.render.Zoom;
import falsify.falsify.utils.ProjectionUtils;
import falsify.falsify.utils.shaders.Shader;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow @Final private Camera camera;

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        EventRender3d eventRender3d = new EventRender3d(tickDelta, matrices, camera, matrices.peek().getPositionMatrix(), buffers);
        Falsify.onEvent(eventRender3d);
        ProjectionUtils.update(tickDelta);
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Zoom zoom = ModuleManager.getModule(Zoom.class);
        if(zoom == null || !zoom.isEnabled()) return;
        cir.setReturnValue(cir.getReturnValue() / zoom.getAmount());
    }


    @Mixin(GameRenderer.class)
    public interface Accessor {
        @Invoker("getFov")
        double getCurrentFov(Camera camera, float tickDelta, boolean changingFov);

    }
}
