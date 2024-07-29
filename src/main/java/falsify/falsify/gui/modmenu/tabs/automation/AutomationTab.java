package falsify.falsify.gui.modmenu.tabs.automation;

import falsify.falsify.Falsify;
import falsify.falsify.automation.Automation;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.gui.modmenu.primitives.ScrollbarWidget;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.ScissorStack;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class AutomationTab extends PanelTab {
    private final Clickable newAutomation;
    private final ScrollbarWidget scrollbarWidget;
    private final ArrayList<AutomationEntry> entries = new ArrayList<>();

    private final double entryHeight = 25;
    private final double entryWidth;
    private final double padding = 2.0;
    public AutomationTab(Panel panel) {
        super(panel);
        this.newAutomation = createNewAutomationButton();
        entryWidth = width - padding * 3 - 10;
        loadModuleEntries();

        double topClamp = y + padding;
        double bottomClamp = y + height - padding * 2;
        double topPoint = entries.get(0).getY();
        double bottomPoint = entries.get(entries.size() - 1).getY() + entryHeight;
        scrollbarWidget = new ScrollbarWidget(panel, x+width-10, topClamp, topClamp, bottomClamp, topPoint, bottomPoint);
        scrollbarWidget.setConsumer(aDouble -> {
            for (AutomationEntry ae : entries) {
                ae.setY(ae.getY() + -1.0 * aDouble / ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            }
        });
    }

    public void loadModuleEntries() {
        for(Module module : ModuleManager.modules.stream().filter(module -> module.category == Category.AUTOMATION).toList()) {
            entries.add(new AutomationEntry(panel, (Automation) module, entryWidth, entryHeight));
        }
        setEntryPositions();
    }

    private void setEntryPositions() {
        double padding = 2.0;
        for(int i = 0; i < entries.size(); i++) {
            AutomationEntry me = entries.get(i);
            me.setX(x + padding);
            me.setWidth(entryWidth);
            me.setY(20 + y + padding + (entryHeight+padding) * i);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ScissorStack scissorStack = panel.getScissorStack();

        panel.drawBackground(context, mouseX, mouseY, delta);
        pushStackToPosition(context.getMatrices());
        FontRenderer fr = Falsify.fontRenderer;
        fr.drawCenteredString(context, "Name", (float) (padding + 5+10), (float) (padding + 3), panel.getTheme().primaryTextColor(), true);
        fr.drawCenteredString(context, "Type", (float) (width / 3), (float) (padding + 3), panel.getTheme().primaryTextColor(), true);
        fr.drawCenteredString(context, "Keybind", (float) (2*width / 3), (float) (padding + 3), panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        scissorStack.push(x, (y+padding), (x+width), (y+height-padding));
        entries.stream().filter(entry -> entry.getY() < y + height && entry.getY() + entry.getHeight() > y).forEach(moduleEntry -> moduleEntry.render(context, mouseX, mouseY, delta));
        scissorStack.pop();

        scrollbarWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(scrollbarWidget.handleClick(x, y, button)) return true;
        for(AutomationEntry ae : entries) {
            if(ae.handleClick(x, y, button)) return true;
        }
        return false;
    }
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(isHovering(mouseX, mouseY)) {
            double topClamp = y + padding;
            double bottomClamp = y + height - padding * 2;
            double topPoint = entries.get(0).getY();
            double bottomPoint = entries.get(entries.size() - 1).getY() + entryHeight;
            if (bottomClamp - topClamp > bottomPoint - topPoint) return true;
            amount *= 10;
            if (entries.size() == 0) return true;
            double scrollDistance = MathUtils.clamp(amount, bottomClamp - bottomPoint, topClamp - topPoint);
            for (AutomationEntry ae : entries) {
                ae.setY(ae.getY() + scrollDistance);
            }
            scrollbarWidget.setY(scrollbarWidget.getY() + scrollDistance * -1.0 * ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean onDrag(double x, double y, int button, double dx, double dy) {
        return scrollbarWidget.onDrag(x, y, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        scrollbarWidget.setActive(false);
        return super.mouseReleased(x, y, button);
    }

    @Override
    public String getName() {
        return "Automation";
    }

    public Clickable createNewAutomationButton() {
        return null;
    }
}
