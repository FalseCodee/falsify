package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Animation;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.gui.DrawContext;

public class WASDModule extends DisplayModule<WASDRenderModule> {

    private final RangeSetting size = new RangeSetting("Size", 20, 10, 100, 1);
    private final RangeSetting padding = new RangeSetting("Padding", 3, 0, 20, 1);
    public WASDModule() {
        super("WASD", "Shows WASD keystrokes.", new WASDRenderModule(2*105.0, 25.0, 160, 105), Category.RENDER, -1, false);
        renderModule.setModule(this);
        settings.add(size);
        settings.add(padding);
    }
    public int getSize(){
        return size.getValue().intValue();
    }

    public int getPadding(){
        return padding.getValue().intValue();
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
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        int size = getModule().getSize();
        int padding = getModule().getPadding();
        int sum = size + padding;
        setWidth(sum*3-padding);
        setHeight(sum*2-padding);
        //W
        if(Falsify.mc.options.forwardKey.isPressed()) wFade.rise(); else wFade.lower();
        drawRect(wFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), context.getMatrices(), (float) 0+sum, (float) 0, (float) (sum + size), (float) (size));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "W", sum + size / 2, size / 2 - Falsify.mc.textRenderer.fontHeight/2, wFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        wFade.tick();

        //A
        if(Falsify.mc.options.leftKey.isPressed()) aFade.rise(); else aFade.lower();
        drawRect(aFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), context.getMatrices(), (float) 0, (float) 0+sum, (float) (size), (float) (sum + size));
        int y1 = sum + size / 2 - Falsify.mc.textRenderer.fontHeight / 2;

        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "A", size / 2, y1, aFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        aFade.tick();

        //S
        if(Falsify.mc.options.backKey.isPressed()) sFade.rise(); else sFade.lower();
        drawRect(sFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), context.getMatrices(), (float) 0+sum, (float) 0+sum, (float) (sum + size), (float) (sum + size));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "S", sum + size / 2, y1, sFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        sFade.tick();

        //D
        if(Falsify.mc.options.rightKey.isPressed()) dFade.rise(); else dFade.lower();
        drawRect(dFade.color(module.getBackgroundColor(), module.getBackgroundColor().brighter().brighter()), context.getMatrices(), (float) 0+sum*2, (float) 0+sum, (float) (sum * 2 + size), (float) (sum + size));
        context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "D", sum * 2 + size / 2, y1, dFade.color(module.getTextColor(), module.getTextColor().darker().darker()).getRGB());
        dFade.tick();
    }
}
