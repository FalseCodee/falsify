package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import falsify.falsify.utils.ScissorStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

public class EventRender extends Event<EventRender> {
    private final float tickDelta;
    private final InGameHud hud;
    private final DrawContext drawContext;
    private final ScissorStack scissorStack;

    public EventRender(float tickDelta, InGameHud hud, DrawContext drawContext) {
        this.tickDelta = tickDelta;
        this.hud = hud;
        this.drawContext = drawContext;
        this.scissorStack = new ScissorStack();
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
    public ScissorStack getScissorStack() {
        return scissorStack;
    }
}
