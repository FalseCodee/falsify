package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class BiomeModule extends DisplayModule<BiomeRenderModule> {
    public BiomeModule() {
        super("Biome", new BiomeRenderModule(0.0, 25.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }
}

class BiomeRenderModule extends RenderModule<BiomeModule> {

    public BiomeRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "Biome: " + ChatModuleUtils.capitalize(Falsify.mc.world.getBiome(Falsify.mc.player.getBlockPos()).getType().name().replace("_", " ")), (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
