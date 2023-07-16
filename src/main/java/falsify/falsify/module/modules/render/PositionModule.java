package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

public class PositionModule extends DisplayModule<PositionRenderModule> {

    private final ModeSetting style = new ModeSetting("Style", "Horizontal", "Horizontal", "Vertical");
    public PositionModule() {
        super("Coordinates ", "Shows your current position.", new PositionRenderModule(0.0, 0.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }

    public String getMode() {
        return style.getMode();
    }
}

class PositionRenderModule extends RenderModule<PositionModule> {

    public PositionRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        BlockPos pos = Falsify.mc.player.getBlockPos();
        if(getModule().getMode().equals("Horizontal")) {
            drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, pos.getX() + ", " + pos.getY() + ", " + pos.getZ(), (int) getX() + (int) width / 2, (int) getY() + (int) height / 2 - Falsify.mc.textRenderer.fontHeight / 2, module.getTextColor().getRGB());
        } else if(getModule().getMode().equals("Vertical")) {
            drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, pos.getX() + ", " + pos.getY() + ", " + pos.getZ(), (int) getX() + (int) width / 2, (int) getY() + (int) height / 2 - Falsify.mc.textRenderer.fontHeight / 2, module.getTextColor().getRGB());
        }

    }
}
