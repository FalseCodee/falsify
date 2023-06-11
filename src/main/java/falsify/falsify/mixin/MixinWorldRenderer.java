package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventRender3d;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    //@Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci, boolean bl, Camera camera, MatrixStack matrixStack, double d, float f, float g, Matrix4f matrix4f, Matrix3f matrix3f) {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci){
        EventRender3d eventRender3d = new EventRender3d(tickDelta, matrices, camera, positionMatrix);
        Falsify.onEvent(eventRender3d);
    }
}
