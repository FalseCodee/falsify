package falsify.falsify.gui.clickgui.primatives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.Draggable;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Tab extends Clickable implements Draggable {

    private final Animation scroll = new Animation(1000, Animation.Type.EASE_IN_OUT);
    private final Category category;
    private boolean extended = false;
    private boolean dragging = false;
    private final ArrayList<ModuleItem> modules = new ArrayList<>();
    private final double moduleWidth;
    private final double moduleHeight;

    public Tab(Category category, int x, int y) {
        super(x, y, 100, 20);
        this.moduleWidth = this.width;
        this.moduleHeight = this.height;
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

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void init() {
        int i = 1;
        for (Module module : ModuleManager.modules.stream().filter(m -> m.category == this.category).toList()){
            modules.add(new ModuleItem(module, x, y + moduleHeight*i, moduleWidth, moduleHeight));
            i++;
        }
        modules.sort(Comparator.comparingInt(module -> -Falsify.mc.textRenderer.getWidth(module.getModule().name)));
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        if (dragging && button == 0) {
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
            drawSmoothRect(scroll.color(category.getColor().darker(), category.getColor()), context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {0, 5, 5, 0});
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, category.getName(), (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
            int i = 1;
            if(scroll.getProgress() != 1.0) enableScissor((int) x, (int) (y+height), (int) (x+width), (int) (y+height + (moduleHeight*modules.size()*scroll.run()) + 1));
            for(ModuleItem moduleItem : modules) {
                moduleItem.setX(x);
                moduleItem.setY(y + moduleHeight*i - (moduleHeight*modules.size()*(1-scroll.run())));
                moduleItem.render(context, mouseX, mouseY, delta, (i == modules.size()));
                i++;
            }

            if(scroll.getProgress() != 1.0) {
                context.fill(0,0,Falsify.mc.getWindow().getScaledWidth(),Falsify.mc.getWindow().getScaledHeight(), Color.WHITE.getRGB());
                disableScissor();
            }
        } else {
            scroll.lower();
            drawSmoothRect(category.getColor().darker(), context.getMatrices(), (int) this.x, (int) this.y, (int) this.x + (int) this.width, (int) this.y + (int) this.height, 2, new int[] {5, 5, 5, 5});
            context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, category.getName(), (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
            if(scroll.run() != 0) {
                int i = 1;
                enableScissor((int) x, (int) (y+height), (int) (x+width), (int) (y+height + (moduleHeight*modules.size()*scroll.run()) + 1));
                for(ModuleItem moduleItem : modules) {
                    moduleItem.setX(x);
                    moduleItem.setY(y + moduleHeight*i - (moduleHeight*modules.size()*(1-scroll.run())));
                    moduleItem.render(context, mouseX, mouseY, delta, (i == modules.size()));
                    i++;
                }
                context.fill(0,0,Falsify.mc.getWindow().getScaledWidth(),Falsify.mc.getWindow().getScaledHeight(), Color.WHITE.getRGB());
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

    public Category getCategory() {
        return this.category;
    }
}
