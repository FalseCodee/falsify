package falsify.falsify.mixin;

import falsify.falsify.Falsify;
import falsify.falsify.gui.ClientMenuScreen;
import falsify.falsify.gui.clickgui.ClickGUI;
import falsify.falsify.listeners.events.EventAttack;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.listeners.events.EventWindowResize;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.render.ESP;
import falsify.falsify.utils.Cape;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {
    @Final
    @Shadow
    private
    Session session;

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;init(Lnet/minecraft/client/MinecraftClient;)V", shift = At.Shift.AFTER))
    public void registerTextures(RunArgs args, CallbackInfo ci){
        Falsify.init(session);
        Falsify.textureCacheManager.registerTextures();
        Cape.addCapes();
        RenderUtils.init();
    }

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if(Falsify.mc.currentScreen instanceof ClickGUI clickGUI) {
            Falsify.configManager.saveModules();
            Falsify.configManager.saveClickGui(clickGUI.getTabs());
            Falsify.configManager.saveConfigFile();
        }
        if(screen == null && Falsify.mc.world == null) return new ClientMenuScreen();
        else if(screen instanceof TitleScreen) return new ClientMenuScreen();
        else return screen;
    }
    @Inject(method = "tick", at =@At("TAIL"))
    public void tick(CallbackInfo ci){
        EventUpdate e = new EventUpdate();
        Falsify.onEvent(e);
    }

    private boolean hasRan = false;

    @Inject(method = "scheduleStop", at = @At("HEAD"))
    public void scheduleStop(CallbackInfo ci) {
        if(hasRan) return;
        Falsify.configManager.saveModules();
        Falsify.configManager.saveConfigFile();
        hasRan = true;
    }
    @Inject(method = "doAttack", at =@At("HEAD"), cancellable = true)
    public void doAttack(CallbackInfoReturnable<Boolean> cir){
        EventAttack e = new EventAttack();
        Falsify.onEvent(e);
        if(e.isCancelled()) cir.setReturnValue(false);
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ESP esp = ModuleManager.getModule(ESP.class);
        if(esp.isEnabled() && esp.isGlow() && esp.isValid(entity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onResolutionChanged", at = @At("HEAD"))
    private void onResolutionChange(CallbackInfo ci){
        EventWindowResize event = new EventWindowResize();
        Falsify.onEvent(event);
    }
}
