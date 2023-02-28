package falsify.falsify.mixin;

import com.google.gson.JsonObject;
import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.ClickGUI;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.utils.DiscordWebhookBuilder;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.NetworkUtils;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey",at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int i, int modifiers, CallbackInfo ci){
        if(MinecraftClient.getInstance().currentScreen == null){
            EventKey e = new EventKey(window, key, scancode, i, modifiers);
            Falsify.onEvent(e);

            if(key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                MinecraftClient.getInstance().setScreen(new ClickGUI());
            }
        }
    }
}
