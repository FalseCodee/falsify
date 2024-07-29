package falsify.falsify.gui.modmenu.tabs.waypoint;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.settings.SettingsTab;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.utils.ScissorStack;
import falsify.falsify.utils.fonts.FontRenderer;
import falsify.falsify.waypoints.Waypoint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class WaypointEntry extends Clickable {
    private static final Color backgroundColor = new Color(31, 31, 31);
    private static final Color toggledOffColor = new Color(255, 94, 94);
    private static final Color toggledOnColor = new Color(83, 255, 75);
    private final Waypoint waypoint;
    private final ScissorStack scissorStack;
    private final Clickable toggleButton;
    private final Clickable settingButton;
    private final Clickable deleteButton;
    private final Panel panel;
    public WaypointEntry(Panel panel, Waypoint waypoint, ScissorStack scissorStack, double width, double height) {
        super(0,0, width, height);
        this.waypoint = waypoint;
        this.toggleButton = createToggleButton();
        this.settingButton = createSettingButton();
        this.deleteButton = createDeleteButton();
        this.scissorStack = scissorStack;
        this.panel = panel;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(toggleButton.handleClick(x, y, button)) return true;
        if(settingButton.handleClick(x, y, button)) return true;
        return deleteButton.handleClick(x, y, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        toggleButton.setX(this.x);
        toggleButton.setY(this.y+this.height-toggleButton.getHeight());

        settingButton.setX(this.x+this.width-20);
        settingButton.setY(this.y+this.height-settingButton.getHeight());

        deleteButton.setX(this.x+this.width-20);
        deleteButton.setY(this.y);

        MatrixStack matrices = context.getMatrices();
        pushStackToPosition(matrices);
        drawSmoothRect(backgroundColor.darker(), matrices, 0,0, (float) width, (float) height, 8, new int[] {10,10,10,10});
        drawSmoothRect(backgroundColor, matrices, 1,1, (float) width-1, (float) height-1, 8, new int[] {10,10,10,10});
        FontRenderer fr = Falsify.fontRenderer;
        fr.drawCenteredString(context, waypoint.getTitle(), (float) (width/2), (float) (height/2-10), Color.WHITE, true);
        matrices.push();
        matrices.translate(width/2, height/2-20, 0);
        matrices.scale(0.75f,0.75f,1);
        scissorStack.push(x, y, x+width, y+height);
        fr.drawCenteredString(context, waypoint.getLocation().toShortString(), 0, 0, Color.WHITE.darker(), true);
        scissorStack.pop();
        matrices.pop();

        matrices.pop();
        toggleButton.render(context, mouseX, mouseY, delta);
        settingButton.render(context, mouseX, mouseY, delta);
        deleteButton.render(context, mouseX, mouseY, delta);
    }

    public Clickable createToggleButton() {
        return new ButtonBuilder()
                .pos(0,0)
                .dimensions(width-20, 20)
                .onClick((clickable, x, y, button)-> {
                    if(!clickable.isHovering(x,y)) return false;
                    waypoint.toggle();
                    return true;
                })
                .onRender(((clickable, context, mouseX, mouseY, delta) -> {
                    Color color = (waypoint.toggled ? toggledOnColor : toggledOffColor);
                    if(clickable.isHovering(mouseX, mouseY)) color = color.brighter();
                    clickable.pushStackToPosition(context.getMatrices());
                    FontRenderer fr = Falsify.fontRenderer;
                    drawSmoothRect(color, context.getMatrices(), 0, 0, (float) clickable.getWidth(), (float) clickable.getHeight(), 8, new int[] {0, 0, 0, 10});
                    drawSmoothRect(color.darker(), context.getMatrices(), 1, 1, (float) clickable.getWidth()-1, (float) clickable.getHeight()-1, 8, new int[] {0, 0, 0, 10});
                    fr.drawCenteredString(context, (waypoint.toggled ? "Enabled" : "Disabled"), (float) (clickable.getWidth()/2), (float) (clickable.getHeight()/2-fr.getStringHeight("E")/2), Color.WHITE, true);
                    context.getMatrices().pop();
                })).build();
    }

    public Clickable createSettingButton() {
        return new ButtonBuilder()
                .pos(0,0)
                .dimensions(20, 20)
                .onClick((clickable, x, y, button)-> {
                    if(!clickable.isHovering(x,y)) return false;
                    panel.setActiveTab(new SettingsTab(panel, waypoint));
                    return true;
                })
                .onRender(((clickable, context, mouseX, mouseY, delta) -> {
                    Color color = (clickable.isHovering(mouseX, mouseY) ? backgroundColor.brighter() : backgroundColor);
                    clickable.pushStackToPosition(context.getMatrices());
                    FontRenderer fr = Falsify.fontRenderer;
                    drawSmoothRect(color.brighter(), context.getMatrices(), 0, 0, (float) clickable.getWidth(), (float) clickable.getHeight(), 8, new int[] {10, 0, 0, 0});
                    drawSmoothRect(color.darker(), context.getMatrices(), 1, 1, (float) clickable.getWidth()-1, (float) clickable.getHeight()-1, 8, new int[] {10, 0, 0, 0});
                    fr.drawCenteredString(context, "S", (float) (clickable.getWidth()/2), (float) (clickable.getHeight()/2-fr.getStringHeight("E")/2), Color.WHITE, true);
                    context.getMatrices().pop();
                })).build();
    }

    public Clickable createDeleteButton() {
        return new ButtonBuilder()
                .pos(0,0)
                .dimensions(20, 20)
                .onClick((clickable, x, y, button)-> {
                    if(!clickable.isHovering(x,y)) return false;
                    Falsify.waypointManager.enabledWaypoints.remove(waypoint);
                    Falsify.waypointManager.getWaypoints().remove(waypoint);

                    WaypointTab panelTab = (WaypointTab) panel.getActiveTab();
                    panelTab.loadModuleEntries();
                    return true;
                })
                .onRender(((clickable, context, mouseX, mouseY, delta) -> {
                    Color color = (clickable.isHovering(mouseX, mouseY) ? panel.getTheme().secondaryColor() : backgroundColor);
                    clickable.pushStackToPosition(context.getMatrices());
                    FontRenderer fr = Falsify.fontRenderer;
                    drawSmoothRect(color.brighter(), context.getMatrices(), 0, 0, (float) clickable.getWidth(), (float) clickable.getHeight(), 8, new int[] {0, 10, 0, 0});
                    drawSmoothRect(color.darker(), context.getMatrices(), 1, 1, (float) clickable.getWidth()-1, (float) clickable.getHeight()-1, 8, new int[] {0, 10, 0, 0});
                    fr.drawCenteredString(context, "X", (float) (clickable.getWidth()/2), (float) (clickable.getHeight()/2-fr.getStringHeight("E")/2), Color.WHITE, true);
                    context.getMatrices().pop();
                })).build();
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }
}
