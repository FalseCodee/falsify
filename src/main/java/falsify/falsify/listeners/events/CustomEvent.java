package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class CustomEvent extends Event<CustomEvent> {
    public final String eventTag;

    public CustomEvent(String eventTag) {
        this.eventTag = eventTag;
    }
}
