package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.FalseRunnable;
import net.minecraft.client.gui.DrawContext;
import falsify.falsify.gui.editor.module.RenderModule;

public class CPSModule extends DisplayModule<CPSRenderModule> {
    private static int rmb = 0;
    private static int lmb = 0;

    public CPSModule() {
        super("CPS", "Shows your Clicks-Per-Second.", new CPSRenderModule(2*105.0, 25.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }

    @Override
    public void onEvent(Event<?> event) {
        super.onEvent(event);
        if(event instanceof EventMouse eventMousePress && eventMousePress.action == 1) {
            if(eventMousePress.button == 0) {
                leftClick();
            } else if(eventMousePress.button == 1) {
                rightClick();
            }
        }
    }

    public static void leftClick() {
        lmb++;
        new FalseRunnable() {
            @Override
            public void run() {
                lmb--;
            }
        }.runTaskLater(1000);
    }

    public static void rightClick() {
        rmb++;
        new FalseRunnable() {
            @Override
            public void run() {
                rmb--;
            }
        }.runTaskLater(1000);
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
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) 0, (float) 0, (float) (width), (float) (height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, module.getLmb() + " | " + module.getRmb(), (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
