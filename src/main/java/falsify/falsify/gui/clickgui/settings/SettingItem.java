package falsify.falsify.gui.clickgui.settings;

import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.module.settings.Setting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public abstract class SettingItem<T extends Setting<?>> extends Clickable {

    protected final T setting;
    protected final Color backgroundColor = new Color(54, 54, 54);
    protected final Color textColor = new Color(255, 255, 255);

    private boolean isActive = false;
    public SettingItem(T setting, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public abstract boolean handleClick(double x, double y, int button);

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
