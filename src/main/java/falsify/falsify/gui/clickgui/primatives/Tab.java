package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Tab extends Clickable implements Draggable {

    private final Animation scroll = new Animation(100, Animation.Type.EASE_IN_OUT);
    private final Category category;
    private boolean extended = false;
    private boolean dragging = false;
    private final ArrayList<ModuleItem> modules = new ArrayList<>();

    public Tab(Category category, int x, int y) {
        super(x, y, 100, 20);
        this.category = category;
        init();
    }


    @Override
    public boolean handleClick(double x, double y, int button) {
        if (isHovering(x, y) && button == 1){
            this.toggleExtended();
            return true;
        }else if(isHovering(x, y) && button == 0) {
            this.dragging = true;
            return true;
        }
         if(isExtended()){
            for(ModuleItem moduleItem : modules) {
                if(moduleItem.handleClick(x, y, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExtended() {
        return extended;
    }

    private void toggleExtended() {
        this.extended = !this.extended;
    }

    public void init() {
        int i = 1;
        for (Module module : ModuleManager.modules.stream().filter(m -> m.category == this.category).collect(Collectors.toList())){
            modules.add(new ModuleItem(module, x, y + 20*i, 100, 20));
            i++;
        }
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if(dragging && button == 0) {
            this.x += dx;
            this.y += dy;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(isExtended()) {
            scroll.rise();
            drawSmoothRect(new Color(54, 54, 54), context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {0, 5, 5, 0});
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, category.getName(), (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
            int i = 1;
            enableScissor((int) x, (int) (y+height), (int) (x+width), (int) (y+height + (20*modules.size()*scroll.run()) + 1));
            for(ModuleItem moduleItem : modules) {
                moduleItem.setX(x);
                moduleItem.setY(y + 20*i - (20*modules.size()*(1-scroll.run())));
                moduleItem.render(context, mouseX, mouseY, delta, (i == modules.size()));
                i++;
            }
            disableScissor();
        } else {
            scroll.lower();
            drawSmoothRect(new Color(54, 54, 54), context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {5, 5, 5, 5});
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, category.getName(), (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
            if(scroll.run() != 0) {
                int i = 1;
                enableScissor((int) x, (int) (y+height), (int) (x+width), (int) (y+height + (20*modules.size()*scroll.run()) + 1));
                for(ModuleItem moduleItem : modules) {
                    moduleItem.setX(x);
                    moduleItem.setY(y + 20*i - (20*modules.size()*(1-scroll.run())));
                    moduleItem.render(context, mouseX, mouseY, delta, (i == modules.size()));
                    i++;
                }
                disableScissor();
            }
        }
        scroll.tick();
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}
