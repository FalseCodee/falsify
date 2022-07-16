package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.gui.clickgui.settings.SettingsGUI;
import falsify.falsify.module.Module;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModuleItem extends Clickable implements Draggable {
    private final Module module;
    public ModuleItem(Module module, double x, double y, double width, double height) {
        super(x, y, width, height);

        this.module = module;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, boolean last) {

        Color color = (module.isEnabled()) ? new Color(109, 206, 54) : new Color(255, 65, 65);
        if(isHovering(mouseX, mouseY)) color = color.brighter();
        if(last) {
            drawSmoothRect(color , matrices, (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {5, 0, 0, 5});
        } else {
            drawRect(color , matrices, (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height);
        }
        drawCenteredText(matrices, Falsify.mc.textRenderer, module.name, (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
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
            if(button == 0) module.toggle();
            else if(button == 1) Falsify.mc.setScreen(new SettingsGUI(module, Falsify.mc.currentScreen));
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        render(matrices, mouseX, mouseY, delta, false);
    }
}
