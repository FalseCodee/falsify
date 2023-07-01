package falsify.falsify.gui.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public interface Renderable {
    void render(DrawContext context, int mouseX, int mouseY, float delta);
}
