package falsify.falsify.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.EventType;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.listeners.events.EventRenderScreen;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.render.Zoom;
import falsify.falsify.utils.ProjectionUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow @Final private Camera camera;

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    public void renderWorld(RenderTickCounter tickCounter, CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        matrices.multiplyPositionMatrix(RenderSystem.getModelViewMatrix());
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        EventRender3d eventRender3d = new EventRender3d(tickCounter.getTickDelta(true), matrices, camera, matrices.peek().getPositionMatrix(), buffers);
        Falsify.onEvent(eventRender3d);
        ProjectionUtils.update(tickCounter.getTickDelta(true));
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderPre(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(Falsify.shaderManager == null) return;
        Falsify.shaderManager.clear();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER))
    private void renderPreScreen(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(Falsify.shaderManager == null) return;
        Falsify.shaderManager.renderWorldShader();
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderAutosaveIndicator(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER))
    private void renderPostScreen(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(Falsify.shaderManager == null) return;

        if(Falsify.mc.currentScreen != null) {
            Falsify.shaderManager.renderGuiShader();
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Zoom zoom = ModuleManager.getModule(Zoom.class);
        if(zoom == null || !zoom.isEnabled()) return;
        cir.setReturnValue(cir.getReturnValue() / zoom.getAmount());
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void onRenderScreenPre(Screen instance, DrawContext context, int mouseX, int mouseY, float delta) {
        EventRenderScreen ersPre = new EventRenderScreen(instance, context, mouseX, mouseY, delta);
        ersPre.setEventType(EventType.PRE);
        Falsify.onEvent(ersPre);

        instance.renderWithTooltip(context, mouseX, mouseY, delta);

        EventRenderScreen ersPost = new EventRenderScreen(instance, context, mouseX, mouseY, delta);
        ersPost.setEventType(EventType.POST);
        Falsify.onEvent(ersPost);
    }

    @Mixin(GameRenderer.class)
    public interface Accessor {
        @Invoker("getFov")
        double getCurrentFov(Camera camera, float tickDelta, boolean changingFov);

    }
}
