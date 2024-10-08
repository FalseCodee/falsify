package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

public class ServerAddressModule extends DisplayModule<ServerAddressRenderModule> {
    public ServerAddressModule() {
        super("Server Address", "Shows the current server address.", new ServerAddressRenderModule(2*105.0, 0.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class ServerAddressRenderModule extends RenderModule<ServerAddressModule> {

    public ServerAddressRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, (Falsify.mc.getCurrentServerEntry() == null || Falsify.mc.getCurrentServerEntry().isLocal()) ? "Local" : Falsify.mc.getCurrentServerEntry().address, (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
