package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.LightType;

public class LightLevelModule extends DisplayModule<LightLevelRenderModule> {
    public LightLevelModule() {
        super("Light Level", "Shows the current light level.", new LightLevelRenderModule(105.0, 25.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class LightLevelRenderModule extends RenderModule<LightLevelModule> {

    public LightLevelRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) 0, (float) 0, (float) (width), (float) (height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "Light Level: " + Falsify.mc.world.getLightLevel(LightType.BLOCK, Falsify.mc.player.getBlockPos()), (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
