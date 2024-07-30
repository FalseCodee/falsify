package falsify.falsify.gui.modmenu.tabs.waypoint;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelTab;
import falsify.falsify.gui.modmenu.primitives.ScrollbarWidget;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.ScissorStack;
import falsify.falsify.waypoints.Waypoint;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class WaypointTab extends PanelTab {

    private final ArrayList<WaypointEntry> waypointEntries = new ArrayList<>();
    private final double padding = 8;
    private int columns = 0;
    private double entryWidth;
    private final double entryHeight = 50;
    private final ScrollbarWidget scrollbarWidget;

    private final Clickable newWaypointButton;


    public WaypointTab(Panel panel) {
        super(panel);
        do {
            columns++;
            this.entryWidth = width / columns - padding * ((double) columns+1)/columns-2f;
        } while (entryWidth > 200);
        loadModuleEntries();
        double topClamp = y + padding + 50;
        double bottomClamp = y + height - padding * 2;
        double topPoint = (waypointEntries.size() == 0) ? 50 + y + padding : waypointEntries.get(0).getY();
        double bottomPoint = (waypointEntries.size() == 0)? y + height - padding * 2 : waypointEntries.get(waypointEntries.size() - 1).getY() + entryHeight;
        scrollbarWidget = new ScrollbarWidget(panel, x+width-10, topClamp, topClamp, bottomClamp, topPoint, bottomPoint);
        scrollbarWidget.setConsumer(aDouble -> {
            for (WaypointEntry me : waypointEntries) {
                me.setY(me.getY() + -1.0 * aDouble / ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            }
        });

        newWaypointButton = new Clickable.ButtonBuilder()
                .pos(x+width-70, y+10)
                .dimensions(60, 20)
                .onClick((instance, x1, y1, button) -> {
                    if(instance.isHovering(x1, y1)) {
                        Waypoint waypoint = new Waypoint("Waypoint #" + (Falsify.waypointManager.getWaypoints().size() + 1), Falsify.mc.player.getBlockX(), Falsify.mc.player.getBlockY(), Falsify.mc.player.getBlockZ());
                        Falsify.waypointManager.getWaypoints().add(waypoint);
                        loadModuleEntries();
                        return true;
                    }
                    return false;
                })
                .onRender((instance, context, mouseX, mouseY, delta) -> {
                    instance.pushStackToPosition(context.getMatrices());
                    Color color = panel.getTheme().secondaryColor();
                    if(instance.isHovering(mouseX, mouseY)) color = color.darker();

                    RenderHelper.drawSmoothRect(color.darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
                    RenderHelper.drawSmoothRect(color, context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
                    Falsify.fontRenderer.drawCenteredString(context, "New Waypoint", (float) instance.getWidth()/2, (float) instance.getHeight()/2-6, panel.getTheme().primaryTextColor(), true);
                    context.getMatrices().pop();
                })
                .build();
    }

    @Override
    public String getName() {
        return "Waypoints";
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(scrollbarWidget.handleClick(x, y, button)) return true;
        if(newWaypointButton.handleClick(x, y, button)) return true;
        for(WaypointEntry me : waypointEntries) {
            if(me.handleClick(x, y, button)) return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ScissorStack scissorStack = panel.getScissorStack();

        panel.drawBackground(context, mouseX, mouseY, delta);
        pushStackToPosition(context.getMatrices());
        context.getMatrices().translate(width/2, 10, 0);
        context.getMatrices().scale(3, 3, 1);
        Falsify.fontRenderer.drawCenteredString(context, "Waypoints", 0, 0, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
        scissorStack.push(x, (y+padding+50), (x+width), (y+height-padding));
        waypointEntries.stream().filter(moduleEntry -> moduleEntry.getY() < y + height && moduleEntry.getY() + moduleEntry.getHeight() > y).forEach(moduleEntry -> moduleEntry.render(context, mouseX, mouseY, delta));
        scissorStack.pop();
        scrollbarWidget.render(context, mouseX, mouseY, delta);
        newWaypointButton.render(context, mouseX, mouseY, delta);
    }

    public void loadModuleEntries() {
        waypointEntries.clear();
        for(Waypoint waypoint : Falsify.waypointManager.getWaypoints()) {
            waypointEntries.add(new WaypointEntry(panel, waypoint, panel.getScissorStack(), entryWidth, entryHeight));
        }
        setEntryPositions();
    }

    private void setEntryPositions() {
        for(int i = 0; i < waypointEntries.size(); i++) {
            WaypointEntry me = waypointEntries.get(i);
            me.setX(x + padding + (padding+entryWidth) * (i%columns));
            me.setWidth(entryWidth);
            me.setY(50 + y + padding + (entryHeight+padding) * (i/columns));
        }
        if(scrollbarWidget == null || waypointEntries.size() == 0) return;

        double topClamp = y + padding + 50;
        double bottomClamp = y + height - padding * 2;
        double topPoint = waypointEntries.get(0).getY();
        double bottomPoint = (waypointEntries.size() == 0)? y + height - padding * 2 : waypointEntries.get(waypointEntries.size() - 1).getY() + entryHeight;
        scrollbarWidget.setY(topClamp);
        scrollbarWidget.setTopClamp(topClamp);
        scrollbarWidget.setBottomClamp(bottomClamp);
        scrollbarWidget.setTopPoint(topPoint);
        scrollbarWidget.setBottomPoint(bottomPoint);
        scrollbarWidget.setBarSize((bottomClamp-topClamp) / (bottomPoint-topPoint) * (bottomClamp-topClamp));

        scrollbarWidget.setConsumer(aDouble -> {
            for (WaypointEntry me : waypointEntries) {
                me.setY(me.getY() + -1.0 * aDouble / ((bottomClamp-topClamp) / (bottomPoint-topPoint)));
            }
        });
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(isHovering(mouseX, mouseY)) {
            double topClamp = y + padding+50;
            double bottomClamp = y + height - padding * 2;
            double topPoint = waypointEntries.get(0).getY();
            double bottomPoint = waypointEntries.get(waypointEntries.size() - 1).getY() + entryHeight;
            if (bottomClamp - topClamp > bottomPoint - topPoint) return true;
            amount *= 10;
            if (waypointEntries.size() == 0) return true;
            double scrollDistance = MathUtils.clamp(amount, bottomClamp - bottomPoint, topClamp - topPoint);
            for (WaypointEntry me : waypointEntries) {
                me.setY(me.getY() + scrollDistance);
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

}
