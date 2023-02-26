package falsify.falsify.module.modules;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.FalseRunnable;
import net.minecraft.client.util.math.MatrixStack;
import falsify.falsify.gui.editor.module.RenderModule;

public class CPSModule extends DisplayModule<CPSRenderModule> {
    private int rmb = 0;
    private int lmb = 0;

    public CPSModule() {
        super("CPS", new CPSRenderModule(2*105.0, 25.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
    }

    @Override
    public void onEvent(Event<?> event) {
        super.onEvent(event);
        if(event instanceof EventMouse eventMousePress && eventMousePress.action == 1) {
            if(eventMousePress.button == 0) {
                lmb++;
                new FalseRunnable() {
                    @Override
                    public void run() {
                        lmb--;
                    }
                }.runTaskLater(1000);
            } else if(eventMousePress.button == 1) {
                rmb++;
                new FalseRunnable() {
                    @Override
                    public void run() {
                        rmb--;
                    }
                }.runTaskLater(1000);
            }
        }
    }

    public int getRmb() {
        return rmb;
    }

    public int getLmb() {
        return lmb;
    }
}

class CPSRenderModule extends RenderModule<CPSModule> {

    public CPSRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), matrices, (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        drawCenteredText(matrices, Falsify.mc.textRenderer, module.getLmb() + " | " + module.getRmb(), (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
