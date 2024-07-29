package falsify.falsify.automation.block;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.utils.Timer;

public class WaitBlock extends AutomationBlock{
    private final Timer timer = new Timer();
    private int delay;

    public WaitBlock(int delay) {
        this.delay = delay;
    }

    @Override
    public void begin() {
        timer.reset();
    }

    @Override
    protected boolean run(Event<?> event) {
        return event instanceof EventUpdate && timer.hasTimeElapsed(delay, false);
    }

    @Override
    public String getName() {
        return "Wait";
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
