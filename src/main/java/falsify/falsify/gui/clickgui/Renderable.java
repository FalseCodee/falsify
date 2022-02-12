package falsify.falsify.gui.clickgui;

import net.minecraft.client.util.math.MatrixStack;

public interface Renderable {
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta);
}
