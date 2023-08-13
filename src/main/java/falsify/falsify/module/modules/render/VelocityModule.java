package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class VelocityModule extends DisplayModule<VelocityRenderModule> {
    public VelocityModule() {
        super("Speed Indicator", "Shows your current velocity.", new VelocityRenderModule(105.0, 25.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class VelocityRenderModule extends RenderModule<VelocityModule> {

    public VelocityRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        ClientPlayerEntity pl = Falsify.mc.player;
        double speed = pl.getPos().distanceTo(new Vec3d(pl.prevX, pl.prevY, pl.prevZ))*20;
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, String.format("%.1f", speed) +" m/s", (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
