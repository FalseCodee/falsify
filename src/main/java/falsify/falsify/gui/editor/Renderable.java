package falsify.falsify.gui.editor;

import net.minecraft.client.util.math.MatrixStack;

public interface Renderable {
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta);
}
