package falsify.falsify.gui.editor.module;

import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ScaleModule extends Clickable implements Draggable {

    private final RenderModule<?> renderModule;
    protected boolean dragging = false;

    public ScaleModule(RenderModule<?> renderModule, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.renderModule = renderModule;
    }

    @Override
    public boolean isHovering(double x, double y) {
        x /= renderModule.scale;
        y /= renderModule.scale;
        return super.isHovering(x, y);
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
            renderModule.setScale(renderModule.getScale() * (x / this.x));
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawSmoothRect(new Color(200,200,200), context.getMatrices(), (float) x, (float) y, (float) (x + width ), (float) (y + height ), (float) (width  / 2), new int[] {10, 10, 10, 10});
    }
}
