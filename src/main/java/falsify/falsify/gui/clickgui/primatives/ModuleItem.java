package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.Module;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModuleItem extends Clickable implements Draggable {
    private final Module module;
    public ModuleItem(Module module, double x, double y, double width, double height) {
        super(x, y, width, height);

        this.module = module;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect((module.isEnabled()) ? new Color(109, 206, 54) : new Color(255, 65, 65) , matrices, (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height);
        drawCenteredText(matrices, Falsify.mc.textRenderer, module.name, (int) x + (int) width/2, (int) y + (int) height/2, 0xffffff);
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        this.x += dx;
        this.y += dy;
        return true;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            module.toggle();
            return true;
        }
        return false;
    }
}
