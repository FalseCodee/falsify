package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

public class ServerPingModule extends DisplayModule<ServerPingRenderModule> {
    public ServerPingModule() {
        super("Server Ping", new ServerPingRenderModule(3*105.00, 0.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class ServerPingRenderModule extends RenderModule<ServerPingModule> {

    public ServerPingRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, (Falsify.mc.getNetworkHandler() == null) ? "Unknown" : "Ping: " + Falsify.mc.getNetworkHandler().getPlayerListEntry(Falsify.mc.player.getUuid()).getLatency() + " ms", (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
