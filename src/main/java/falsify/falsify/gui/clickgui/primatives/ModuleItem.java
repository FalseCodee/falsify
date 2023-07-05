package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.gui.clickgui.settings.SettingsGUI;
import falsify.falsify.module.Module;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModuleItem extends Clickable implements Draggable {
    private final Module module;
    private final Animation fade = new Animation(100, Animation.Type.LINEAR);
    private final Animation fade2 = new Animation(50, Animation.Type.LINEAR);
    public ModuleItem(Module module, double x, double y, double width, double height) {
        super(x, y, width, height);

        this.module = module;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta, boolean last) {
        Color inactive = module.category.getColor().darker();
        Color active = module.category.getColor().brighter().brighter();
        if(module.isEnabled()) fade.rise(); else fade.lower();
        Color color = fade.color(inactive, active);
        if(isHovering(mouseX, mouseY)){
            fade2.rise();
            renderDescription(context, mouseX, mouseY, delta);
        } else fade2.lower();
        color = fade2.color(color, color.brighter());
        if(last) {
            drawSmoothRect(color.darker() , context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {5, 0, 0, 5});
            drawSmoothRect(color , context.getMatrices(), (int) this.x+1, (int) this.y, (int) this.x + (int) this.width-1, (int) this.y + (int) this.height-1, 2, new int[] {5, 0, 0, 5});
        } else {
            drawRect(color.darker() , context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height);
            drawRect(color , context.getMatrices(), (int) this.x+1, (int) this.y, (int) this.x + (int) this.width-1, (int) this.y + (int) this.height-1);

        }
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, module.name, (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
        fade.tick();
        fade2.tick();
    }

    private void renderDescription(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        int padding = 4;
        matrices.translate(mouseX, mouseY-Falsify.mc.textRenderer.fontHeight - 2 * padding, 0.35);
        String description = module.description;
        RenderHelper.drawSmoothRect(module.category.getColor().darker(),matrices,-1,-1, Falsify.mc.textRenderer.getWidth(description) + 2 * padding + 1, Falsify.mc.textRenderer.fontHeight + 2 * padding + 1, padding/2f, new int[] {5,5,5,5});
        RenderHelper.drawSmoothRect(module.category.getColor(),matrices,0,0, Falsify.mc.textRenderer.getWidth(description) + 2 * padding, Falsify.mc.textRenderer.fontHeight + 2 * padding, padding/2f, new int[] {5,5,5,5});
        context.drawTextWithShadow(Falsify.mc.textRenderer, description, padding, padding, Color.WHITE.getRGB());
        matrices.pop();
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        render(context, mouseX, mouseY, delta, false);
    }

    public Module getModule() {
        return this.module;
    }
}
