package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.gui.DrawContext;

public class BiomeModule extends DisplayModule<BiomeRenderModule> {
    public BiomeModule() {
        super("Biome", "Shows your current biome.", new BiomeRenderModule(0.0, 25.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class BiomeRenderModule extends RenderModule<BiomeModule> {

    public BiomeRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "Biome: " + ChatModuleUtils.capitalize(Falsify.mc.world.getBiome(Falsify.mc.player.getBlockPos()).getType().name().replace("_", " ")), (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
