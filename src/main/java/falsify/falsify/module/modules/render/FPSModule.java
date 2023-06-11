package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.util.math.MatrixStack;

public class FPSModule extends DisplayModule<FPSRenderModule> {
    public FPSModule() {
        super("FPS", new FPSRenderModule(0.0, 0.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class FPSRenderModule extends RenderModule<FPSModule> {

    public FPSRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), matrices, (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        drawCenteredTextWithShadow(matrices, Falsify.mc.textRenderer, "FPS: " + ((MixinMinecraft)Falsify.mc).getCurrentFps(), (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
