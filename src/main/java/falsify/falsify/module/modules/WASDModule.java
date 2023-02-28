package falsify.falsify.module.modules;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.FalseRunnable;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class WASDModule extends DisplayModule<WASDRenderModule> {

    RangeSetting size = new RangeSetting("Size", 20, 10, 100, 1);
    RangeSetting padding = new RangeSetting("Padding", 3, 0, 20, 1);
    public WASDModule() {
        super("WASD", new WASDRenderModule(2*105.0, 25.0, 160, 105), Category.RENDER, -1);
        renderModule.setModule(this);
        settings.add(size);
        settings.add(padding);
    }
}

class WASDRenderModule extends RenderModule<WASDModule> {

    public WASDRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int size = getModule().size.getValue().intValue();
        int padding = getModule().padding.getValue().intValue();
        int sum = size + padding;
        this.width = sum*3-padding;
        this.height = sum*2-padding;
        //W
        drawRect((Falsify.mc.options.forwardKey.isPressed()) ? Color.WHITE : module.getBackgroundColor(), matrices, (float) getX()+sum, (float) getY(), (float) (getX()+sum + size), (float) (getY() + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "W", (int) getX()+sum + size/2, (int) getY() + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, (Falsify.mc.options.forwardKey.isPressed()) ? Color.BLACK.getRGB() : module.getTextColor().getRGB());

        //A
        drawRect((Falsify.mc.options.leftKey.isPressed()) ? Color.WHITE : module.getBackgroundColor(), matrices, (float) getX(), (float) getY()+sum, (float) (getX() + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "A", (int) getX() + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, (Falsify.mc.options.leftKey.isPressed()) ? Color.BLACK.getRGB() : module.getTextColor().getRGB());

        //S
        drawRect((Falsify.mc.options.backKey.isPressed()) ? Color.WHITE : module.getBackgroundColor(), matrices, (float) getX()+sum, (float) getY()+sum, (float) (getX()+sum + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "S", (int) getX()+sum + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, (Falsify.mc.options.backKey.isPressed()) ? Color.BLACK.getRGB() : module.getTextColor().getRGB());

        //D
        drawRect((Falsify.mc.options.rightKey.isPressed()) ? Color.WHITE : module.getBackgroundColor(), matrices, (float) getX()+sum*2, (float) getY()+sum, (float) (getX()+sum*2 + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "D", (int) getX()+sum*2 + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, (Falsify.mc.options.rightKey.isPressed()) ? Color.BLACK.getRGB() : module.getTextColor().getRGB());

    }
}
