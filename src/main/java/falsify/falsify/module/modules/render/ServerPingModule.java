package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

public class ServerPingModule extends DisplayModule<ServerPingRenderModule> {
    public ServerPingModule() {
        super("Server Ping", "Shows the current server ping.", new ServerPingRenderModule(3*105.00, 0.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class ServerPingRenderModule extends RenderModule<ServerPingModule> {

    public ServerPingRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        int ping;
        if(Falsify.mc.getNetworkHandler() == null || Falsify.mc.player == null || Falsify.mc.getNetworkHandler().getPlayerListEntry(Falsify.mc.player.getUuid()) == null) ping = 0;
        else ping = Falsify.mc.getNetworkHandler().getPlayerListEntry(Falsify.mc.player.getUuid()).getLatency();
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "Ping: " + ping + " ms", (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
