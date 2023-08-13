package falsify.falsify.module.settings;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventSettingChange;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<List<String>> {

    private int index;

    public ModeSetting(String name, String def, String... modes) {
        super(name);
        this.value = Arrays.asList(modes);
        this.index = value.indexOf(def);
    }

    public void cycle() {
        this.index = (this.index + 1) % this.value.size();
        EventSettingChange<ModeSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
        EventSettingChange<ModeSetting> event = new EventSettingChange<>(this);
        Falsify.onEvent(event);
    }

    public String getMode() {
        return getValue().get(index);
    }

}
