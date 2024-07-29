package falsify.falsify.gui.modmenu.settings.widgets.automation;

import falsify.falsify.Falsify;
import falsify.falsify.automation.block.SendChatMessageBlock;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelWidget;
import falsify.falsify.gui.modmenu.primitives.TextBoxWidget;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

public class SendMessageBlockWidget extends PanelWidget implements Typable {

    private final TextBoxWidget textBoxWidget;
    private final SendChatMessageBlock messageBlock;
    public SendMessageBlockWidget(Panel panel, SendChatMessageBlock messageBlock, double x, double y, double width) {
        super(panel, x, y, width, 20);
        this.messageBlock = messageBlock;

        this.textBoxWidget = new TextBoxWidget(panel, x+width-160, y+height/2-6, 150, 12, ((instance, context, mouseX, mouseY, delta) -> {
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor().darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor(), context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
        }));
        textBoxWidget.setText(messageBlock.getMessage());
        textBoxWidget.setChangedListener(messageBlock::setMessage);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());
        context.fill(0, 0, (int) width, (int) height, panel.getTheme().primaryColor().darker().getRGB());
        context.getMatrices().scale(1.2f, 1.2f, 1);
        Falsify.fontRenderer.drawString(context, messageBlock.getName(), 5, 2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        textBoxWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(textBoxWidget.handleClick(x, y, button)) return true;
        return super.handleClick(x, y, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return textBoxWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return textBoxWidget.charTyped(chr, modifiers);
    }
}
