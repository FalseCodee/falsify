package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ArmorModule extends DisplayModule<ArmorRenderModule> {

    public ArmorModule() {
        super("Armor", "Shows the status of your equipped armor.", new ArmorRenderModule(3*105.0, 25.0, 3 + 20 + mc.textRenderer.getWidth("888") + 6, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
    }
}

class ArmorRenderModule extends RenderModule<ArmorModule> {

    public ArmorRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        module.drawBackground(context, mouseX, mouseY, delta);
        renderArmor(context);
    }

    private void renderArmor(DrawContext context) {
        int padding = 3;
        double y = padding;

        for(int i = Falsify.mc.player.getInventory().armor.size()-1; i >= 0; i--) {
            ItemStack itemStack = Falsify.mc.player.getInventory().armor.get(i);
            if(itemStack.isEmpty()) continue;
            context.drawItem(itemStack, (int) (double) padding, (int) y);
            context.drawTextWithShadow(Falsify.mc.textRenderer, Text.of("" + (itemStack.getMaxDamage() - itemStack.getDamage())), (int) (double) padding + 20, (int)y + Falsify.mc.textRenderer.fontHeight/2, module.getTextColor().getRGB());
            y += 16 + padding;
        }

        this.height = Math.max(y, 16 + padding);
    }
}
