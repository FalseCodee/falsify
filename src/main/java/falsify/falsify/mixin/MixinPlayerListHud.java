package falsify.falsify.mixin;

import falsify.falsify.module.modules.render.Icons;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public abstract class MixinPlayerListHud {
    @Shadow public abstract Text getPlayerName(PlayerListEntry entry);

    @Inject(method = "renderLatencyIcon", at = @At(value = "HEAD"))
    private void renderIcon(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if(!Icons.users.contains(entry.getProfile().getId())) return;
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        Icons.render(context);
        context.getMatrices().pop();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    private Text getText(PlayerListHud instance, PlayerListEntry entry) {
        if(!Icons.users.contains(entry.getProfile().getId())) return getPlayerName(entry);
        return ChatModuleUtils.join(Text.of("   "), getPlayerName(entry));
    }
}
