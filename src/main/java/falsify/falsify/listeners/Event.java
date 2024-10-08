package falsify.falsify.listeners;

public class Event<T> {
    public boolean cancelled;
    public EventType eventType;
    public EventDirection eventDirection;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventDirection getEventDirection() {
        return eventDirection;
    }

    public void setEventDirection(EventDirection eventDirection) {
        this.eventDirection = eventDirection;
    }

    public boolean isPre() {
        return eventType == EventType.PRE;
    }

    public boolean isPost() {
        return eventType == EventType.POST;
    }





}
