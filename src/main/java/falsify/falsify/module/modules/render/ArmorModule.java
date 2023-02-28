package falsify.falsify.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ArmorModule extends DisplayModule<ArmorRenderModule> {

    public ArmorModule() {
        super("Armor", new ArmorRenderModule(3*105.0, 25.0, 3 + 20 + mc.textRenderer.getWidth("888") + 6, 20), Category.PLAYER, -1);
        renderModule.setModule(this);
    }
}

class ArmorRenderModule extends RenderModule<ArmorModule> {

    public ArmorRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawRect(module.getBackgroundColor(), matrices, (float) getX(), (float) getY(), (float) (getX() + width), (float) (getY() + height));
        renderArmor(matrices);
    }

    private void renderArmor(MatrixStack matrices) {
        int padding = 3;
        double x = getX() + padding;
        double y = getY() + padding;

        for(int i = Falsify.mc.player.getInventory().armor.size()-1; i >= 0; i--) {
            ItemStack itemStack = Falsify.mc.player.getInventory().armor.get(i);
            if(itemStack.isEmpty()) continue;
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.push();
            RenderHelper.convertToScale(matrixStack, scale);
            RenderSystem.applyModelViewMatrix();
            Falsify.mc.getItemRenderer().renderInGui(itemStack, (int) x, (int) y);
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
            drawTextWithShadow(matrices, Falsify.mc.textRenderer, Text.of("" + (itemStack.getMaxDamage() - itemStack.getDamage())), (int)x + 20, (int)y + Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
            y += 16 + padding;
        }
        this.height = Math.max(y - this.y, 16 + padding);
    }
}
