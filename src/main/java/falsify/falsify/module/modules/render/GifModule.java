package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.GifReader;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class GifModule extends DisplayModule<GifRenderModule> {

    private GifReader gif;
    public final RangeSetting scale = new RangeSetting("Scale", 0.5, 0.01, 3, 0.01);
    public GifModule() {
        super("GIF", "Shows a funny gif.", new GifRenderModule(105.0, 25.0, 100, 20), Category.RENDER, -1, false);
        setGif("https://media.discordapp.net/attachments/472407075419586560/1043625078518194288/1565269137873670144.gif");
        settings.removeIf(RangeSetting.class::isInstance);
        settings.add(scale);
        renderModule.setModule(this);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend ep && ep.getPacket() instanceof ChatMessageC2SPacket packet && packet.chatMessage().toLowerCase().startsWith(".gif ")) {
            String urlString = packet.chatMessage().substring(".gif ".length());
            setGif(urlString);
            ep.setCancelled(true);
        } else super.onEvent(event);
    }

    public void setGif(String url) {
        if(gif != null) {
            gif.destroy();
            gif = null;
        }
        new FalseRunnable() {
            @Override
            public void run() {
                gif = new GifReader("gif", url);
            }
        }.runTaskAsync();
    }

    public LegacyIdentifier getCurrentFrame() {
        if(gif == null) {
            renderModule.setWidth(100);
            renderModule.setHeight(100);
            return null;
        }
        LegacyIdentifier id = gif.getCurrentFrame();
        if(id == null) return null;

        renderModule.setWidth(id.getWidth()*scale.getValue());
        renderModule.setHeight(id.getHeight()*scale.getValue());
        return id;
    }
}

class GifRenderModule extends RenderModule<GifModule> {

    public GifRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        LegacyIdentifier frame = module.getCurrentFrame();
        if(frame == null) return;

        double width = frame.getWidth() * module.scale.getValue();
        double height = frame.getHeight() * module.scale.getValue();
        RenderUtils.drawTexture(frame, context.getMatrices(), 0,0,0,0, (int) width, (int) height, (int) width, (int) height);
    }
}
