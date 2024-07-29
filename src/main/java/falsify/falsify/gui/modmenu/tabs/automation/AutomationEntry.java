package falsify.falsify.gui.modmenu.tabs.automation;

import falsify.falsify.Falsify;
import falsify.falsify.automation.Automation;
import falsify.falsify.automation.types.GameMessageReceivedAutomation;
import falsify.falsify.automation.types.KeyPressAutomation;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.settings.SettingsTab;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.client.gui.DrawContext;

public class AutomationEntry extends Clickable {
    private final Panel panel;
    private final Automation automation;
    public AutomationEntry(Panel panel, Automation automation, double width, double height) {
        super(0, 0, width, height);
        this.panel = panel;
        this.automation = automation;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(!isHovering(x, y)) return false;
        panel.setActiveTab(new SettingsTab(panel, automation));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());
        context.fill(0, 0, (int) width, (int) height, panel.getTheme().secondaryColor().getRGB());
        Falsify.fontRenderer.drawString(context, automation.name, 3, 3, panel.getTheme().primaryTextColor(), true);
        Falsify.fontRenderer.drawCenteredString(context, getType(), (float) (width / 3)+7, 3, panel.getTheme().primaryTextColor(), true);
        Falsify.fontRenderer.drawCenteredString(context, ChatModuleUtils.keyCodeToString(automation.keybind.getValue()), (float) (2*width / 3)+9, 3, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();
    }

    public String getType() {
        if(automation instanceof KeyPressAutomation) return "Key Press";
        if(automation instanceof GameMessageReceivedAutomation) return "Chat Listen";
        return "Unknown";
    }
}
