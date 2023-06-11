package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.LightType;

public class LightLevelModule extends DisplayModule<LightLevelRenderModule> {
    public LightLevelModule() {
        super("Light Level", new LightLevelRenderModule(105.0, 25.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class LightLevelRenderModule extends RenderModule<LightLevelModule> {

    public LightLevelRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), matrices, (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        drawCenteredTextWithShadow(matrices, Falsify.mc.textRenderer, "Light Level: " + Falsify.mc.world.getLightLevel(LightType.BLOCK, Falsify.mc.player.getBlockPos()), (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
