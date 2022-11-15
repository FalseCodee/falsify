package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public class EventRender extends Event<EventRender> {
    final float tickDelta;
    final InGameHud hud;
    final MatrixStack matrixStack;

    public EventRender(float tickDelta, InGameHud hud, MatrixStack matrixStack) {
        this.tickDelta = tickDelta;
        this.hud = hud;
        this.matrixStack = matrixStack;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public InGameHud getHud() {
        return hud;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }
}
