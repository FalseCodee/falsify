package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.util.Window;

import static falsify.falsify.Falsify.mc;

public class EventWindowResize extends Event<EventWindowResize> {
    private final int prevScaledWidth;
    private final int prevScaledHeight;
    private final int newScaledWidth;
    private final int newScaledHeight;

    public EventWindowResize() {
        Window w = mc.getWindow();
        this.prevScaledWidth = w.getScaledWidth();
        this.prevScaledHeight = w.getScaledHeight();
        int sf = mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
        int i = (int)((double)w.getFramebufferWidth() / sf);
        this.newScaledWidth = (double)w.getFramebufferWidth() / sf > (double)i ? i + 1 : i;
        int j = (int)((double)w.getFramebufferHeight() / sf);
        this.newScaledHeight = (double)w.getFramebufferHeight() / sf > (double)j ? j + 1 : j;
    }

    public int getPrevScaledWidth() {
        return prevScaledWidth;
    }

    public int getPrevScaledHeight() {
        return prevScaledHeight;
    }

    public int getNewScaledWidth() {
        return newScaledWidth;
    }

    public int getNewScaledHeight() {
        return newScaledHeight;
    }
}
