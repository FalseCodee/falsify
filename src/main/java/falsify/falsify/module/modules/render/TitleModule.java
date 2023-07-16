package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.LegacyIdentifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class TitleModule extends DisplayModule<TitleRenderModule> {
    public TitleModule() {
        super("Title", "Shows the client title", new TitleRenderModule(0.0, 25.0, 100, 25), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class TitleRenderModule extends RenderModule<TitleModule> {

    LegacyIdentifier title = null;

    public TitleRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private void setTitle() {
        CompletableFuture<LegacyIdentifier> futureTitle = Falsify.textureCacheManager.getIdentifier("title");
        if(futureTitle.isDone()) {
            this.title = futureTitle.getNow(null);
            System.out.println("Title done: " + (title == null));
        }
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        if(title == null) {
            setTitle();
            return;
        }
        drawRect(module.getBackgroundColor(), context.getMatrices(), (float) 0, (float) 0, (float) (width), (float) (height));
        context.drawTexture(title, (int)0,(int)0, 0,0,(int)width, (int)height, (int)width, (int)height);
    }
}
