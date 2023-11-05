package falsify.falsify.gui.editor.module;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Draggable;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ScaleModule extends Clickable implements Draggable {

    private final RenderModule<?> renderModule;
    private final RangeSetting scale = new RangeSetting("Range", 1.0, 0.5, 3, 0.05);
    protected boolean dragging = false;

    public ScaleModule(RenderModule<?> renderModule, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.renderModule = renderModule;
    }

    @Override
    public boolean isHovering(double x, double y) {
        return x >= getX() && y >= getY() && x <= getX() + getWidth() * renderModule.getScale() && y <= getY() + getHeight() * renderModule.getScale();
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)){
            if(!dragging) dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(dragging) {
            scale.setValue(renderModule.getScale() * (x / this.x));
            renderModule.setScale(scale.getValue());
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawSmoothRect(new Color(200,200,200), context.getMatrices(), (float) 0, (float) 0, (float) (width), (float) (height), (float) (width  / 2), new int[] {10, 10, 10, 10});
        if(dragging) context.drawText(Falsify.mc.textRenderer, "Scale: " + scale.getValue(), 7, 0, -1, true);
    }
}
