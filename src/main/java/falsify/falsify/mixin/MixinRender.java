package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventMovementTick;
import falsify.falsify.listeners.events.EventRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinRender {
    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        EventRender e = new EventRender(tickCounter.getTickDelta(true), Falsify.mc.inGameHud, context);
        Falsify.onEvent(e);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void frame(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        EventMovementTick e = new EventMovementTick();
        Falsify.onEvent(e);
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    public void renderSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        ci.cancel();
    }
}
