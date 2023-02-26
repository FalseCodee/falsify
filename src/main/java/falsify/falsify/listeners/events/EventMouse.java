package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class EventMouse extends Event<EventMouse> {
    public final long window;
    public final int button;
    public final int action;
    public final int mods;

    public EventMouse(long window, int button, int action, int mods) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
    }
}
