package falsify.falsify.gui.modmenu.tabs.modlist;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.TextBoxWidget;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelWidget;
import falsify.falsify.gui.utils.Scrollable;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.module.Category;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.function.Predicate;

public class FilterWidget extends PanelWidget implements Scrollable, Typable {

    private final ArrayList<Clickable> filterButtons = new ArrayList<>();
    private final TextBoxWidget textBoxWidget;
    private final ModTab parent;
    public FilterWidget(Panel panel, double x, double y, double width, double height, ModTab parent) {
        super(panel, x, y, width, height);
        this.parent = parent;
        this.textBoxWidget = new TextBoxWidget(panel, x + 5, y + 20, width-10, 12, ((instance, context, mouseX, mouseY, delta) -> {
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor().darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor(), context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
        }));

        textBoxWidget.setChangedListener((s -> parent.setFilter(moduleEntry -> s.length() == 0 || moduleEntry.getModule().name.toLowerCase().contains(s.toLowerCase()) || moduleEntry.getModule().description.toLowerCase().contains(s.toLowerCase()))));
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
                .pos(x + padding, y + padding + 35 + (buttonHeight + padding) * filterButtons.size())
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
                    context.fill(0,0, (int) instance.getWidth(), (int) instance.getHeight(), panel.getTheme().secondaryColor().getRGB());
                    Falsify.fontRenderer.drawCenteredString(context, name, (float) (instance.getWidth()/2f), (float) (instance.getHeight()/2f)-Falsify.fontRenderer.getStringHeight(name)/2f, panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                }).build();
        filterButtons.add(clickable);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(textBoxWidget.handleClick(x, y, button)) return true;
        for(Clickable clickable : filterButtons) {
            if(clickable.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, (float) width, (float) height, 7, new int[] {0, 0, 10, 10});
        Falsify.fontRenderer.drawCenteredString(context, "Filters", (float) (width/2f), 5, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
        enableScissor(x+5, y+5+35, x+width-5, y+height-5);
        filterButtons.forEach(filterButton -> filterButton.render(context, mouseX, mouseY, delta));
        disableScissor();

        textBoxWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(!isHovering(mouseX, mouseY)) return false;
        double topClamp = y + 48;
        double bottomClamp = y + height - 5 * 2;
        double topPoint = filterButtons.get(0).getY();
        double bottomPoint = filterButtons.get(filterButtons.size() - 1).getY() + filterButtons.get(filterButtons.size() - 1).getHeight();
        if(bottomClamp-topClamp > bottomPoint-topPoint) return true;
        amount *= 10;
        if (filterButtons.size() == 0) return true;
        double scrollDistance = MathUtils.clamp(amount, bottomClamp - bottomPoint, topClamp - topPoint);
        for (Clickable fb : filterButtons) {
            fb.setY(fb.getY() + scrollDistance);
        }
        return true;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return textBoxWidget.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return textBoxWidget.keyPressed(keyCode, scanCode, modifiers);
    }
}
