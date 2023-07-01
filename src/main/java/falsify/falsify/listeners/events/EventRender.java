package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

public class EventRender extends Event<EventRender> {
    final float tickDelta;
    final InGameHud hud;
    final DrawContext drawContext;

    public EventRender(float tickDelta, InGameHud hud, DrawContext drawContext) {
        this.tickDelta = tickDelta;
        this.hud = hud;
        this.drawContext = drawContext;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public InGameHud getHud() {
        return hud;
    }

    public DrawContext getDrawContext() {
        return drawContext;
    }
}
