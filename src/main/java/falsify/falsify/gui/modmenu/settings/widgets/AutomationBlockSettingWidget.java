package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.automation.block.AutomationBlock;
import falsify.falsify.automation.block.SendChatMessageBlock;
import falsify.falsify.automation.block.WaitBlock;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelWidget;
import falsify.falsify.gui.modmenu.settings.widgets.automation.SendMessageBlockWidget;
import falsify.falsify.gui.modmenu.settings.widgets.automation.WaitBlockWidget;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.module.settings.AutomationBlockSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class AutomationBlockSettingWidget extends SettingWidget<AutomationBlockSetting> implements Typable {

    private final ArrayList<PanelWidget> blockWidgets = new ArrayList<>();
    public AutomationBlockSettingWidget(AutomationBlockSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 2);
        initializeBlocks();
    }

    private void initializeBlocks() {
        blockWidgets.clear();
        ArrayList<AutomationBlock> blocks = setting.getValue();

        for(int i = 0; i < blocks.size(); i++) {
            AutomationBlock block = blocks.get(i);

            if(block instanceof WaitBlock) {
                blockWidgets.add(new WaitBlockWidget(panel, (WaitBlock) block, x + 2, y + height, width/2 - 2));
                height += 20;
            }
            else if(block instanceof SendChatMessageBlock) {
                blockWidgets.add(new SendMessageBlockWidget(panel, (SendChatMessageBlock) block, x + 2, y + height, width/2 - 2));
                height += 20;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());

        RenderUtils.fillXGradient(context, 50, (int) height+1, (int) width, (int) height+2, 0, panel.getTheme().primaryColor().darker().getRGB());
        context.getMatrices().pop();

        blockWidgets.forEach(blockWidget -> blockWidget.render(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        for(PanelWidget widget : blockWidgets)
            if(widget.handleClick(x, y, button)) return true;
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for(PanelWidget widget : blockWidgets)
            if(widget.charTyped(chr, modifiers)) return true;
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(PanelWidget widget : blockWidgets)
            if(widget instanceof Typable typable && typable.keyPressed(keyCode, scanCode, modifiers)) return true;
        return false;
    }
}
