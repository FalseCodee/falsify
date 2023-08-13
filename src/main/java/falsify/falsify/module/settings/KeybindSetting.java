package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventSettingChange;

public class KeybindSetting extends Setting<Integer>{

    public KeybindSetting(String name, Integer value) {
        super(name);
        this.value = value;
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(value);
        EventSettingChange<KeybindSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }
}
