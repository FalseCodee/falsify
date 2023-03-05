package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.primatives.Animation;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class WASDModule extends DisplayModule<WASDRenderModule> {

    RangeSetting size = new RangeSetting("Size", 20, 10, 100, 1);
    RangeSetting padding = new RangeSetting("Padding", 3, 0, 20, 1);
    public WASDModule() {
        super("WASD", new WASDRenderModule(2*105.0, 25.0, 160, 105), Category.RENDER, -1);
        renderModule.setModule(this);
        settings.add(size);
        settings.add(padding);
    }
}

class WASDRenderModule extends RenderModule<WASDModule> {

    private final Animation wFade = new Animation(100, Animation.Type.EASE_OUT);
    private final Animation aFade = new Animation(100, Animation.Type.EASE_OUT);
    private final Animation sFade = new Animation(100, Animation.Type.EASE_OUT);
    private final Animation dFade = new Animation(100, Animation.Type.EASE_OUT);

    public WASDRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int size = getModule().size.getValue().intValue();
        int padding = getModule().padding.getValue().intValue();
        int sum = size + padding;
        this.width = sum*3-padding;
        this.height = sum*2-padding;
        //W
        if(Falsify.mc.options.forwardKey.isPressed()) wFade.rise(); else wFade.lower();
        drawRect(wFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), matrices, (float) getX()+sum, (float) getY(), (float) (getX()+sum + size), (float) (getY() + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "W", (int) getX()+sum + size/2, (int) getY() + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, wFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        wFade.tick();

        //A
        if(Falsify.mc.options.leftKey.isPressed()) aFade.rise(); else aFade.lower();
        drawRect(aFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), matrices, (float) getX(), (float) getY()+sum, (float) (getX() + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "A", (int) getX() + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, aFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        aFade.tick();

        //S
        if(Falsify.mc.options.backKey.isPressed()) sFade.rise(); else sFade.lower();
        drawRect(sFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), matrices, (float) getX()+sum, (float) getY()+sum, (float) (getX()+sum + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "S", (int) getX()+sum + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, sFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        sFade.tick();

        //D
        if(Falsify.mc.options.rightKey.isPressed()) dFade.rise(); else dFade.lower();
        drawRect(dFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), matrices, (float) getX()+sum*2, (float) getY()+sum, (float) (getX()+sum*2 + size), (float) (getY()+sum + size));
        drawCenteredText(matrices, Falsify.mc.textRenderer, "D", (int) getX()+sum*2 + (int) size/2, (int) getY()+sum + (int) size/2 - Falsify.mc.textRenderer.fontHeight/2, dFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        dFade.tick();
    }
}
