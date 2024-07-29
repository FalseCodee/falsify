package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;

public class EventChatSend extends Event<EventChatSend> {
    public String message;

    public EventChatSend(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
