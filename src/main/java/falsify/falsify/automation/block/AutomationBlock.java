package falsify.falsify.automation.block;

import falsify.falsify.listeners.Event;

public abstract class AutomationBlock {

    private boolean complete = false;

    protected abstract boolean run(Event<?> event);
    public abstract String getName();
    public void onEvent(Event<?> event) {
        complete = run(event);
    }

    public void reset() {
        complete = false;
    }

    public void begin() {

    }

    public void end() {

    }

    public boolean isComplete() {
        return complete;
    }
}
