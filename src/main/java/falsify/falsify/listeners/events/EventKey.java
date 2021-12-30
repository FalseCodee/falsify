package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class EventKey extends Event<EventKey> {
    long window;
    int key;

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
        return i;
    }

    public int getModifiers() {
        return modifiers;
    }

    int scancode;
    int i;
    int modifiers;


    public EventKey(long window, int key, int scancode, int i, int modifiers) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.i = i;
        this.modifiers = modifiers;
    }




}
