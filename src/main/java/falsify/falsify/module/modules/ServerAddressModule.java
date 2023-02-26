package falsify.falsify.module.modules;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.util.math.MatrixStack;

public class ServerAddressModule extends DisplayModule<ServerAddressRenderModule> {
    public ServerAddressModule() {
        super("Server Address", new ServerAddressRenderModule(2*105.0, 0.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class ServerAddressRenderModule extends RenderModule<ServerAddressModule> {

    public ServerAddressRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), matrices, (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        drawCenteredText(matrices, Falsify.mc.textRenderer, (Falsify.mc.getCurrentServerEntry() == null || Falsify.mc.getCurrentServerEntry().isLocal()) ? "Local" : Falsify.mc.getCurrentServerEntry().address, (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
