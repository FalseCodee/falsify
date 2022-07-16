package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventUpdate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {
    @Final
    @Shadow
    private
    Session session;

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;inGameHud:Lnet/minecraft/client/gui/hud/InGameHud;", ordinal = 0, shift = At.Shift.AFTER))
    public void init(RunArgs args, CallbackInfo ci){
        Falsify.init(session);
    }
    @Inject(method = "tick", at =@At("TAIL"))
    public void tick(CallbackInfo ci){
        EventUpdate e = new EventUpdate();
        Falsify.onEvent(e);
    }
}
