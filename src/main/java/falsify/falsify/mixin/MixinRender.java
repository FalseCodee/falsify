package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventMovementTick;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.utils.shaders.Framebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinRender {
    @Unique
    private Framebuffer framebuffer;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderAutosaveIndicator(Lnet/minecraft/client/gui/DrawContext;)V", ordinal = 0, shift = At.Shift.AFTER))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci){
        EventRender e = new EventRender(tickDelta, Falsify.mc.inGameHud, context);
        Falsify.onEvent(e);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void frame(DrawContext context, float tickDelta, CallbackInfo ci){
        EventMovementTick e = new EventMovementTick();
        Falsify.onEvent(e);
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    public void renderSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        ci.cancel();
    }
}
