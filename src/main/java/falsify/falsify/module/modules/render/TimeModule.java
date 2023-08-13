package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;

import java.util.Calendar;

public class TimeModule extends DisplayModule<TimeRenderModule> {
    public TimeModule() {
        super("Current Time", "Shows the current time", new TimeRenderModule(4*105.0, 0.0, 100, 20), Category.RENDER, -1, false);
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
        module.drawBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, getTime(), (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }

    private String getTime() {
        int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        //int min = Calendar.getInstance().get(Calendar.MINUTE);

        String afterNoonStatus = (hr >= 12) ? "PM" : "AM";

        return String.format("%tl:%tM %s",Calendar.getInstance(), Calendar.getInstance(), afterNoonStatus);
    }
}
