package falsify.falsify.module;

import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventWindowResize;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class DisplayModule<T extends RenderModule<?>> extends Module {

    protected final T renderModule;
    protected final ColorSetting backgroundColor = new ColorSetting("Background", 30,30,30,100);
    protected final ColorSetting textColor = new ColorSetting("Text", 255,255,255,255);
    protected final ModeSetting borderType = new ModeSetting("Border Type", "Rectangle", "Rectangle", "Smooth");
    protected final BooleanSetting outline = new BooleanSetting("Draw Outline", false);


    public DisplayModule(String name, String description, T renderModule, Category category, int keyCode, boolean isCheat){
        super(name, description, isCheat, category, keyCode);
        this.renderModule = renderModule;
        settings.add(borderType);
        settings.add(outline);
        settings.add(backgroundColor);
        settings.add(textColor);
    }

    public DisplayModule(String name, String description, T renderModule, Category category, int keyCode, boolean enabled, boolean isCheat){
        super(name, description, category, keyCode, enabled, isCheat);
        this.renderModule = renderModule;
    }

    public void onEvent(Event<?> event){
         if(event instanceof EventRender eventRender) {
             textColor.tick();
             backgroundColor.tick();

             if(mc.currentScreen == null || mc.currentScreen.getClass() != EditGUI.class) {
                 MatrixStack matrices = eventRender.getDrawContext().getMatrices();
                 matrices.push();
                 //RenderHelper.convertToScale(eventRender.getMatrixStack());
                 renderModule.render(eventRender.getDrawContext(), 0, 0, eventRender.getTickDelta());
                 matrices.pop();
             }
         } else if (event instanceof EventWindowResize e) {
             renderModule.setX(renderModule.getX() / e.getPrevScaledWidth() * e.getNewScaledWidth());
             renderModule.setY(renderModule.getY() / e.getPrevScaledHeight() * e.getNewScaledHeight());
         }
    }

    public void drawBackground(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        if(outline.getValue()) {
            drawBackgroundInternal(context, getBackgroundColor().darker(), mouseX, mouseY, tickDelta, 0, 0, (float) renderModule.getWidth(), (float) renderModule.getHeight());
            drawBackgroundInternal(context, getBackgroundColor(), mouseX, mouseY, tickDelta, 1, 1, (float) renderModule.getWidth()-1, (float) renderModule.getHeight()-1);
        } else {
            drawBackgroundInternal(context, getBackgroundColor(), mouseX, mouseY, tickDelta, 0, 0, (float) renderModule.getWidth(), (float) renderModule.getHeight());
        }
    }

    public void drawBackgroundInternal(DrawContext context, Color color, int mouseX, int mouseY, float tickDelta, float x, float y, float width, float height) {
        switch (borderType.getMode()) {
            case "Rectangle" -> RenderHelper.drawRect(color, context.getMatrices(), x, y, (int) width, (int) height);
            case "Smooth" -> RenderHelper.drawSmoothRect(color, context.getMatrices(), x, y, width, height, 5, new int[] {10, 10, 10, 10});
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor.getValue();
    }

    public Color getTextColor() {
        return textColor.getValue();
    }

    public T getRenderModule(){
        return renderModule;
    }
}
