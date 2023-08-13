package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.gui.modmenu.primitives.PanelWidget;
import falsify.falsify.module.settings.Setting;

public abstract class SettingWidget<T extends Setting<?>> extends PanelWidget {
    protected final T setting;
    public SettingWidget(T setting, Panel panel, double x, double y, double width, double height) {
        super(panel, x, y, width, height);
        this.setting = setting;
    }
}
