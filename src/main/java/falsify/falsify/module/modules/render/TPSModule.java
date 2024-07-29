package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TPSModule extends DisplayModule<TPSRenderModule> {

    private final float[] tpsSamples = new float[20];
    private int index = 0;
    private long lastTime = -1;


    public TPSModule() {
        super("TPS", "Shows the server's ticks per second.", new TPSRenderModule(105.0, 25.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }

    @Override
    public void onEvent(Event<?> event) {
        super.onEvent(event);

        if(event instanceof EventPacketRecieve eventPacketRecieve && eventPacketRecieve.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            long time = System.currentTimeMillis();
            float deltaTime = (time - lastTime) / 1000.0f;

            tpsSamples[index] = MathUtils.clamp(20.0f / deltaTime, 0, 20);
            index++;
            index %= tpsSamples.length;
            lastTime = time;
        }
    }

    public float getTps() {
        float sum = 0.0f;
        int ticksCounted = 0;
        for(float tick : tpsSamples) {
            if(tick == 0.0) continue;
            sum += tick;
            ticksCounted++;
        }
        return sum / ticksCounted;
    }
}

class TPSRenderModule extends RenderModule<TPSModule> {

    public TPSRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, String.format("%.1f", module.getTps()) +" TPS", (int) width/2, (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
    }
}
