package falsify.falsify.module;

import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.settings.ColorSetting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class DisplayModule<T extends RenderModule<?>> extends Module {

    protected final T renderModule;
    protected ColorSetting backgroundColor = new ColorSetting("Background", 30,30,30,100);
    protected ColorSetting textColor = new ColorSetting("Text", 255,255,255,255);

    public DisplayModule(String name, T renderModule, Category category, int keyCode){
        super(name, category, keyCode);
        this.renderModule = renderModule;

        settings.add(backgroundColor.getRed());
        settings.add(backgroundColor.getGreen());
        settings.add(backgroundColor.getBlue());
        settings.add(backgroundColor.getAlpha());

        settings.add(textColor.getRed());
        settings.add(textColor.getGreen());
        settings.add(textColor.getBlue());
        settings.add(textColor.getAlpha());
    }

    public DisplayModule(String name, T renderModule, Category category, int keyCode, boolean enabled){
        super(name, category, keyCode, enabled);
        this.renderModule = renderModule;
    }

    public void onEvent(Event<?> event){
         if(event instanceof EventRender eventRender) {
             if(mc.currentScreen == null || mc.currentScreen.getClass() != EditGUI.class) {
                 MatrixStack matrices = eventRender.getDrawContext().getMatrices();
                 matrices.push();
                 //RenderHelper.convertToScale(eventRender.getMatrixStack());
                 renderModule.render(eventRender.getDrawContext(), 0, 0, eventRender.getTickDelta());
                 matrices.pop();
             }
         }
    }

    public Color getBackgroundColor() {
        return backgroundColor.getColor();
    }

    public Color getTextColor() {
        return textColor.getColor();
    }

    public T getRenderModule(){
        return renderModule;
    }
}
