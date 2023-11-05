package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class EventKey extends Event<EventKey> {
    final long window;
    final int key;
    final int scancode;
    final int action;
    final int modifiers;


    public EventKey(long window, int key, int scancode, int action, int modifiers) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public long getWindow() {
        return window;
    }

    public int getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }

    public int getModifiers() {
        return modifiers;
    }


}
