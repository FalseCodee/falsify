package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.gui.hud.InGameHud;
public class EventRender extends Event<EventRender> {
    float tickDelta;
    InGameHud hud;
    public EventRender(InGameHud hud, float tickDelta) {
        this.hud = hud;
        this.tickDelta = tickDelta;
    }


    public InGameHud getHud() {
        return hud;
    }

    public float getTickDelta() {
        return tickDelta;
    }

}
