package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class EventRenderScreen extends Event<EventRenderScreen> {
    private final Screen screen;
    private final DrawContext drawContext;
    private final int mouseX;
    private final int mouseY;
    private final float tickDelta;

    public EventRenderScreen(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        this.screen = screen;
        this.drawContext = drawContext;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.tickDelta = tickDelta;
    }

    public Screen getScreen() {
        return screen;
    }

    public DrawContext getDrawContext() {
        return drawContext;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
