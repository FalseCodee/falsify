package falsify.falsify.gui.modmenu;

import falsify.falsify.gui.modmenu.primitives.FilterWidget;
import falsify.falsify.gui.modmenu.primitives.ModPanel;
import falsify.falsify.gui.modmenu.primitives.ModuleEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class ModMenuScreen extends Screen {
    private ModPanel panel;
    private FilterWidget filterWidget;

    public ModMenuScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        panel = new ModPanel(width/2f-width/8f,height/2f-height/4f, width/2f,height/2f);
        filterWidget = new FilterWidget(width/4f,height/2f-height/4f, width/8f,height/2f, this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(panel.handleClick(mouseX, mouseY, button)) return true;
        if(filterWidget.handleClick(mouseX, mouseY, button)) return true;
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return panel.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        panel.render(context, mouseX, mouseY, delta);
        filterWidget.render(context, mouseX, mouseY, delta);
    }

    public void setFilter(Predicate<? super ModuleEntry> predicate) {
        panel.setFilter(predicate);
    }
}
