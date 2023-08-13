package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventSettingChange;

public class BooleanSetting extends Setting<Boolean>{

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
        EventSettingChange<BooleanSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value);
        EventSettingChange<BooleanSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }
}
