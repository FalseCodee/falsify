package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

public class PlayersOnlineModule extends DisplayModule<PlayersOnlineRenderModule> {
    public PlayersOnlineModule() {
        super("Player Count", "Shows the current player count.", new PlayersOnlineRenderModule(1*105.0, 0.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class PlayersOnlineRenderModule extends RenderModule<PlayersOnlineModule> {

    public PlayersOnlineRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, (Falsify.mc.getNetworkHandler() == null) ? "Unknown" : Falsify.mc.getNetworkHandler().getPlayerList().size() + " Players", (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
