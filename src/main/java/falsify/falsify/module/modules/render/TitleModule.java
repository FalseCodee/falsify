package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.DiscordWebhookBuilder;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.gui.DrawContext;

import java.util.concurrent.CompletableFuture;

public class TitleModule extends DisplayModule<TitleRenderModule> {
    public TitleModule() {
        super("Shill", "Shill for daddy mineflows", new TitleRenderModule(0.0, 25.0, 100, 100), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class TitleRenderModule extends RenderModule<TitleModule> {

    LegacyIdentifier title = null;

    public TitleRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private void setTitle() {
        if(title == null) {
            this.title = Falsify.textureCacheManager.getIdentifier("pizza-hut");
        }
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        if(title == null) {
            setTitle();
            return;
        }
        module.drawBackground(context, mouseX, mouseY, delta);
        context.drawTexture(title, 0, 0, 1,1,(int)width, (int)height, (int)width+2, (int)height+1);
    }
}
