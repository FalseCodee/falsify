package falsify.falsify.gui.editor;

import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditGUI extends Screen {
    List<RenderModule<?>> renderModules = new ArrayList<>();

    public EditGUI() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
       renderModules.clear();

        ModuleManager.modules.stream()
                .filter(DisplayModule.class::isInstance)
                .forEach(module -> renderModules.add(((DisplayModule<?>) module).getRenderModule()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderHelper.drawRect(new Color(50,50,50, 100), matrices, 0, 0, width, height);
        matrices.push();
        RenderHelper.convertToScale(matrices);
        for(int i = renderModules.size()-1; i >= 0; i--) {
            renderModules.get(i).render(matrices, mouseX, mouseY, delta);
        }
        matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(RenderModule<?> renderModule : renderModules) {
            if(renderModule.handleClick(mouseX * RenderHelper.getScaleFactor(), mouseY * RenderHelper.getScaleFactor(), button)) {
                renderModules.remove(renderModule);
                renderModules.add(0, renderModule);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(RenderModule<?> renderModule : renderModules) {
            if(renderModule.onDrag(mouseX * RenderHelper.getScaleFactor(), mouseY * RenderHelper.getScaleFactor(), button, deltaX * RenderHelper.getScaleFactor(), deltaY * RenderHelper.getScaleFactor())) {

                return true;
            }
        }
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(RenderModule<?> renderModule : renderModules) {
            renderModule.setDragging(false);
            renderModule.setScaleDragging(false);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
