package falsify.falsify.gui.modmenu.tabs.modlist;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.settings.SettingsTab;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ScissorStack;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModuleEntry extends Clickable {
    private static final Color backgroundColor = new Color(31, 31, 31);
    private static final Color toggledOffColor = new Color(255, 94, 94);
    private static final Color toggledOnColor = new Color(83, 255, 75);
    private final Module module;
    private final ScissorStack scissorStack;
    private final Clickable toggleButton;
    private final Clickable settingButton;
    private final Panel panel;
    public ModuleEntry(Panel panel, Module module, ScissorStack scissorStack, double width, double height) {
        super(0,0, width, height);
        this.module = module;
        this.toggleButton = createToggleButton();
        this.settingButton = createSettingButton();
        this.scissorStack = scissorStack;
        this.panel = panel;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(toggleButton.handleClick(x, y, button)) return true;
        return settingButton.handleClick(x, y, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        toggleButton.setX(this.x);
        toggleButton.setY(this.y+this.height-toggleButton.getHeight());

        settingButton.setX(this.x+this.width-20);
        settingButton.setY(this.y+this.height-settingButton.getHeight());

        MatrixStack matrices = context.getMatrices();
        pushStackToPosition(matrices);
        drawSmoothRect(backgroundColor.darker(), matrices, 0,0, (float) width, (float) height, 8, new int[] {10,10,10,10});
        drawSmoothRect(backgroundColor, matrices, 1,1, (float) width-1, (float) height-1, 8, new int[] {10,10,10,10});
        FontRenderer fr = Falsify.fontRenderer;
        fr.drawCenteredString(context, module.name, (float) (width/2), (float) (height/2-10), Color.WHITE, true);
        matrices.push();
        matrices.translate(width/2, height/2-20, 0);
        matrices.scale(0.75f,0.75f,1);
        scissorStack.push(x, y, x+width, y+height);
        fr.drawCenteredString(context, module.description, 0, 0, Color.WHITE.darker(), true);
        scissorStack.pop();
        matrices.pop();

        matrices.pop();
        toggleButton.render(context, mouseX, mouseY, delta);
        settingButton.render(context, mouseX, mouseY, delta);
    }

    public Clickable createToggleButton() {
        return new Clickable.ButtonBuilder()
                .pos(0,0)
                .dimensions(width-20, 20)
                .onClick((clickable, x, y, button)-> {
                    if(!clickable.isHovering(x,y)) return false;
                    module.toggle();
                    return true;
                })
                .onRender(((clickable, context, mouseX, mouseY, delta) -> {
                    Color color = (module.toggled ? toggledOnColor : toggledOffColor);
                    if(clickable.isHovering(mouseX, mouseY)) color = color.brighter();
                    clickable.pushStackToPosition(context.getMatrices());
                    FontRenderer fr = Falsify.fontRenderer;
                    drawSmoothRect(color, context.getMatrices(), 0, 0, (float) clickable.getWidth(), (float) clickable.getHeight(), 8, new int[] {0, 0, 0, 10});
                    drawSmoothRect(color.darker(), context.getMatrices(), 1, 1, (float) clickable.getWidth()-1, (float) clickable.getHeight()-1, 8, new int[] {0, 0, 0, 10});
                    fr.drawCenteredString(context, (module.toggled ? "Enabled" : "Disabled"), (float) (clickable.getWidth()/2), (float) (clickable.getHeight()/2-fr.getStringHeight("E")/2), Color.WHITE, true);
                    context.getMatrices().pop();
                })).build();
    }

    public Clickable createSettingButton() {
        return new Clickable.ButtonBuilder()
                .pos(0,0)
                .dimensions(20, 20)
                .onClick((clickable, x, y, button)-> {
                    if(!clickable.isHovering(x,y)) return false;
                    panel.setActiveTab(new SettingsTab(panel, module));
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

    public Module getModule() {
        return module;
    }
}
