package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.modmenu.ModMenuScreen;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class FilterWidget extends Clickable {

    private final ArrayList<Clickable> filterButtons = new ArrayList<>();
    private final ModMenuScreen parent;
    public FilterWidget(double x, double y, double width, double height, ModMenuScreen parent) {
        super(x, y, width, height);
        this.parent = parent;
        addFilterButton("All", moduleEntry -> true);
        addFilterButton("Movement", moduleEntry -> moduleEntry.getModule().category == Category.MOVEMENT);
        addFilterButton("Player", moduleEntry -> moduleEntry.getModule().category == Category.PLAYER);
        addFilterButton("Combat", moduleEntry -> moduleEntry.getModule().category == Category.COMBAT);
        addFilterButton("Render", moduleEntry -> moduleEntry.getModule().category == Category.RENDER);
        addFilterButton("Misc", moduleEntry -> moduleEntry.getModule().category == Category.MISC);
        addFilterButton("Non-Cheats", moduleEntry -> !moduleEntry.getModule().isCheat);
    }

    private void addFilterButton(String name, Predicate<? super ModuleEntry> predicate) {
        int padding = 5;
        int buttonHeight = 20;
        Clickable clickable = new Clickable.ButtonBuilder()
                .pos(x + padding, y + padding + (buttonHeight + padding) * filterButtons.size())
                .dimensions(width-padding*2, buttonHeight)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1,y1)) {
                        parent.setFilter(predicate);
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    context.getMatrices().push();
                    context.getMatrices().translate(instance.getX(), instance.getY(), 0);
                    context.fill(0,0, (int) instance.getWidth(), (int) instance.getHeight(), Color.WHITE.getRGB());
                    Falsify.fontRenderer.drawCenteredString(context.getMatrices(), name, (float) (instance.getWidth()/2f), (float) (instance.getHeight()/2f)-Falsify.fontRenderer.getStringHeight(name)/2f, Color.BLACK, true);
                    context.getMatrices().pop();
                }).build();
        filterButtons.add(clickable);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        for(Clickable clickable : filterButtons) {
            if(clickable.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.fill(0,0, (int) width, (int) height, new Color(100,100,100).getRGB());
        context.getMatrices().pop();
        filterButtons.forEach(filterButton -> filterButton.render(context, mouseX, mouseY, delta));
    }
}
