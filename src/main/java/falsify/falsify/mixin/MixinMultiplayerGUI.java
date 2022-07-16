package falsify.falsify.mixin;

import falsify.falsify.gui.BungeeGUI;
import falsify.falsify.gui.ServerPingGUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerGUI extends Screen {
    protected MixinMultiplayerGUI(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;updateButtonActivationStates()V", ordinal = 0, shift = At.Shift.BEFORE))
    public void onInit(CallbackInfo ci){
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 4 + 76, 10, 75, 20, Text.of("bungee"), (buttonWidget) -> {
            assert this.client != null;
            this.client.setScreen(new BungeeGUI(client.currentScreen));
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 4 - 76 -75, 10, 75, 20, Text.of("ping"), (buttonWidget) -> {
            assert this.client != null;
            this.client.setScreen(new ServerPingGUI());
        }));
    }
}
