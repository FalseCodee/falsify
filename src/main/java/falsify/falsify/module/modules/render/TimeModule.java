package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

import java.util.Calendar;

public class TimeModule extends DisplayModule<TimeRenderModule> {
    public TimeModule() {
        super("Current Time", new TimeRenderModule(4*105.0, 0.0, 100, 20), Category.RENDER, -1);
        renderModule.setModule(this);
        renderModule.setScale(2);
    }
}

class TimeRenderModule extends RenderModule<TimeModule> {

    public TimeRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, getTime(), (int) getX() + (int) width/2, (int) getY() + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }

    private String getTime() {
        int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);

        String afterNoonStatus = (hr >= 12) ? "PM" : "AM";
        hr = (hr > 12) ? hr - 12 :
                (hr == 0 || hr == 12) ? 12 : 0;

        return String.format("%d:%02d %s", hr, min, afterNoonStatus);
    }
}
