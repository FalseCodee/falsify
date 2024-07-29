package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.TextBoxWidget;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.module.settings.TextSetting;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Predicate;

public class TextSettingWidget extends SettingWidget<TextSetting> implements Typable {

    private final TextBoxWidget textBoxWidget;
    public TextSettingWidget(TextSetting setting, Panel panel, double x, double y, double width, Predicate<String> predicate) {
        super(setting, panel, x, y, width, 16);
        this.textBoxWidget = new TextBoxWidget(panel, x+width - 120, y+height / 2 - 6,100, 12, ((instance, context, mouseX, mouseY, delta) -> {
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor().darker(), context.getMatrices(), -2, -2, (float) instance.getWidth()+2, (float) instance.getHeight()+2, 5, new int[] {10, 10, 10, 10});
            RenderHelper.drawSmoothRect(Falsify.theme.primaryColor(), context.getMatrices(), 0, 0, (float) instance.getWidth(), (float) instance.getHeight(), 3, new int[] {10, 10, 10, 10});
        }));
        this.textBoxWidget.setText(setting.getValue());
        this.textBoxWidget.setTextPredicate(predicate);
        this.textBoxWidget.setChangedListener(setting::setValue);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        return textBoxWidget.handleClick(x, y, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());
        RenderUtils.fillXGradient(context, 50, (int) height+1, (int) width, (int) height+2, 0, panel.getTheme().primaryColor().darker().getRGB());
        context.getMatrices().push();
        context.getMatrices().scale(1.2f, 1.2f, 1);
        fr.drawString(context, setting.getName(), 5F, (float) height/2-fr.getStringHeight(setting.getName())*1.2f/2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
        context.getMatrices().pop();

        textBoxWidget.setY(y+height / 2 - 6);
        textBoxWidget.render(context, mouseX, mouseY, delta);

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
