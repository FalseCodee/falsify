package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Animation;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class BooleanSettingWidget extends SettingWidget<BooleanSetting> {
    private final Color disabledColor = new Color(255, 67, 67);
    private final Color enabledColor = new Color(68, 253, 39);
    private final Animation animation = new Animation(100, Animation.Type.EASE_IN_OUT);
    public BooleanSettingWidget(BooleanSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 25);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            setting.toggle();
        }
        return false;
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

        if(setting.getValue()) animation.rise();
        else animation.lower();
        animation.tick();

        float progress = (float) animation.run();

        context.getMatrices().translate(width-10-30, height/2-5, 0);
        drawSmoothRect(disabledColor, context.getMatrices(), (20*progress), 0, ((20*1.0f) + 10), 10, 5, new int[] {10,10,10,10});
        drawSmoothRect(enabledColor, context.getMatrices(), 0, 0, ((20*progress) + 10), 10, 5, new int[] {10,10,10,10});

        drawSmoothRect(Color.WHITE.darker(), context.getMatrices(), (20*progress)-1, -1, ((20*progress) + 10)+1, 10+1, 5.5f, new int[] {10,10,10,10});
        drawSmoothRect(Color.WHITE, context.getMatrices(), (20*progress), 0, ((20*progress) + 10), 10, 5, new int[] {10,10,10,10});

        context.getMatrices().pop();
    }
}
