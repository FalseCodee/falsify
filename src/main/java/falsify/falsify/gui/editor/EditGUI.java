package falsify.falsify.gui.editor;

import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.gui.editor.module.Snapper;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EditGUI extends Screen {
    private final List<RenderModule<?>> renderModules = new ArrayList<>();
    private final Snapper snapper = new Snapper();

    public EditGUI() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
       renderModules.clear();

        ModuleManager.modules.stream()
                .filter(DisplayModule.class::isInstance)
                .forEach(module -> renderModules.add(((DisplayModule<?>) module).getRenderModule()));
        snapper.update(RenderHelper.WINDOW.getScaledWidth()/2f, RenderHelper.WINDOW.getScaledHeight()/2f);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderHelper.drawRect(new Color(50,50,50, 100), context.getMatrices(), 0, 0, width, height);
        for(int i = renderModules.size()-1; i >= 0; i--) {
            renderModules.get(i).render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(RenderModule<?> renderModule : renderModules) {
            if(renderModule.handleClick(mouseX, mouseY, button)) {
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
            if(renderModule.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                if(!renderModule.isDragging()) return true;
                boolean xSnap = renderModule.horizontalSnap(snapper);
                boolean ySnap = renderModule.verticalSnap(snapper);
                List<RenderModule<?>> sorted = new ArrayList<>(renderModules.stream().filter(renderModule1 -> renderModule1 != renderModule).sorted(Comparator.comparingDouble(renderModule1 -> Math.pow(renderModule.getX() - renderModule1.getX(), 2) + Math.pow(renderModule.getY() - renderModule1.getY(), 2))).toList());

                for(int i = 0; i < sorted.size(); i++) {
                    RenderModule<?> renderModule2 = sorted.get(i);
                    renderModule2.getSnapper().update();
                    if(!xSnap) xSnap = renderModule.horizontalSnap(renderModule2.getSnapper());
                    if(!ySnap) ySnap = renderModule.verticalSnap(renderModule2.getSnapper());
                    if(xSnap && ySnap) break;
                }
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
