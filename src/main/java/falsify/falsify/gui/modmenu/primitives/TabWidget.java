package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class TabWidget extends PanelWidget{
    private final List<PanelTab> panels;
    private final List<Clickable> tabs;
    public TabWidget(Panel panel, List<PanelTab> tabs, double x, double y, double width, double height) {
        super(panel, x, y-height, width, height);
        this.panels = tabs;
        this.tabs = new ArrayList<>();
        tabs.forEach(this::addTab);
    }

    private void addTab(PanelTab panelTab) {
        double tabWidth = width / panels.size();
        Clickable tab = new Clickable.ButtonBuilder()
                .pos(x + tabWidth * tabs.size(), y)
                .dimensions(tabWidth, height)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1, y1)) {
                        panel.setActiveTab(panelTab);
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    instance.pushStackToPosition(context.getMatrices());
                    drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 4.0f, new int[] {0, 10, 10, 0});
                    Falsify.fontRenderer.drawCenteredString(context, panelTab.getName(), (float) instance.getWidth()/2, (float) instance.getHeight()/2-Falsify.fontRenderer.getStringHeight(panelTab.getName())/2, panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                }).build();
        tabs.add(tab);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        tabs.forEach(clickable -> clickable.render(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        for(Clickable tab : tabs) {
            if(tab.handleClick(x, y, button)) return true;
        }
        return super.handleClick(x, y, button);
    }
}
