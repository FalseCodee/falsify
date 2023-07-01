package falsify.falsify.gui.clickgui;

import net.minecraft.client.gui.DrawContext;

public interface Renderable {
    void render(DrawContext context, int mouseX, int mouseY, float delta);
}
