package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.gui.utils.Animation;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class CheckboxWidget extends PanelWidget {
    private boolean isChecked = false;

    private Consumer<Boolean> booleanConsumer;
    private final Animation scaleUp = new Animation(100, Animation.Type.EASE_OUT);
    public CheckboxWidget(Panel panel, double x, double y, double width, double height) {
        super(panel, x, y, width, height);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x,y)) {
            this.isChecked = !isChecked;
            if(booleanConsumer != null) booleanConsumer.accept(isChecked);
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());

        float halfWidth = (float) (width/2);
        float halfHeight = (float) (height/2);
        context.getMatrices().translate(halfWidth, halfHeight, 0);
        drawSmoothRect(panel.getTheme().primaryColor().darker().darker(), context.getMatrices(), -halfWidth, -halfHeight, halfWidth, halfHeight, 3, new int[] {8,8,8,8});
        drawSmoothRect(panel.getTheme().primaryColor().darker(), context.getMatrices(), -halfWidth+2, -halfHeight+2, halfWidth-2, halfHeight-2, 2, new int[] {8,8,8,8});
        if(isChecked) {
            scaleUp.rise();
        } else {
            scaleUp.lower();
        }
        context.getMatrices().scale((float) scaleUp.run(), (float) scaleUp.run(), 0);
        drawSmoothRect(panel.getTheme().secondaryColor(), context.getMatrices(), -halfWidth+2, -halfHeight+2, halfWidth-2, halfHeight-2, 2, new int[] {8,8,8,8});

        scaleUp.tick();
        context.getMatrices().pop();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Consumer<Boolean> getBooleanConsumer() {
        return booleanConsumer;
    }

    public void setBooleanConsumer(Consumer<Boolean> booleanConsumer) {
        this.booleanConsumer = booleanConsumer;
    }
}
