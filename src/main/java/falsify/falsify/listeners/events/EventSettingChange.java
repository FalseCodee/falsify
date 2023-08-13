package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import falsify.falsify.module.settings.Setting;

public class EventSettingChange<T extends Setting<?>> extends Event<EventSettingChange<?>> {
    private final T setting;

    public EventSettingChange(T setting) {
        this.setting = setting;
    }

    public T getSetting() {
        return setting;
    }
}
