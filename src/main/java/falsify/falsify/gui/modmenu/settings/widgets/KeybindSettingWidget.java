package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.module.settings.KeybindSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

public class KeybindSettingWidget extends SettingWidget<KeybindSetting> implements Typable {

    private boolean binding = false;
    public KeybindSettingWidget(KeybindSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 30);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            binding = !binding;
            return true;
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
        fr.drawString(context, setting.getName(), 5, 2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        fr.drawString(context, "Current bind:", 5, 17, panel.getTheme().secondaryTextColor(), true);
        fr.drawString(context, ChatModuleUtils.keyCodeToString(setting.getValue()), 5 + fr.getStringWidth("Current bind:") + 2, 17, panel.getTheme().primaryTextColor(), true);

        if(binding) {
            String bindMessage = "Binding. Press ESC or click to cancel.";
            String resetMessage = "Press BACKSPACE to reset.";
            fr.drawCenteredString(context, bindMessage, (float) (width/2f), (float) (height/2f - fr.getStringHeight(bindMessage)/2f)-7, panel.getTheme().primaryTextColor(), true);
            fr.drawCenteredString(context, resetMessage, (float) (width/2f), (float) (height/2f - fr.getStringHeight(resetMessage)/2f)+7, panel.getTheme().primaryTextColor(), true);
        } else  {
            String bindMessage = "Click to bind.";
            fr.drawString(context, bindMessage, (float) (width - fr.getStringWidth(bindMessage) - 10), (float) (height/2f - fr.getStringHeight(bindMessage)/2f), panel.getTheme().primaryTextColor(), true);
        }
        context.getMatrices().pop();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(binding) {
            if(keyCode == 259) setting.setValue(-1);
            else if (keyCode != 256) setting.setValue(keyCode);
            binding = false;
            return true;
        }
        return false;
    }
}
