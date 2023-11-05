package falsify.falsify.module.modules.render;

import com.google.common.collect.Ordering;
import falsify.falsify.Falsify;
import falsify.falsify.gui.editor.module.RenderModule;
import falsify.falsify.module.Category;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.settings.RangeSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PotionEffectModule extends DisplayModule<PotionEffectRenderModule> {
    private final RangeSetting padding = new RangeSetting("Padding", 10, 0, 50, 0.1);
    public PotionEffectModule() {
        super("Potion Effects", "Shows active potion effects.", new PotionEffectRenderModule(2*105.0, 0.0, 100, 20), Category.RENDER, -1, false);
        renderModule.setModule(this);
        settings.add(padding);
    }

    public double getPadding() {
        return padding.getValue();
    }
}

class PotionEffectRenderModule extends RenderModule<PotionEffectModule> {

    public PotionEffectRenderModule(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void renderModule(DrawContext context, int mouseX, int mouseY, float delta) {
        drawStatusEffects(context, mouseX, mouseY, delta);
    }

    private void drawStatusEffects(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = -2;
        int j = (int) (this.width - i);
        Collection<StatusEffectInstance> collection = Falsify.mc.player.getStatusEffects();
        if (collection.isEmpty() || j < 32) {
            return;
        }
        module.drawBackground(context, mouseX, mouseY, delta);
        boolean bl = j >= 120;
        int k = (int) (18+module.getPadding());
        if (collection.size() > 5) {
            k = Math.max((k*4) / (collection.size() - 1), 18);
        }
        List<StatusEffectInstance> iterable = Ordering.natural().sortedCopy(collection);
        this.drawStatusEffectSprites(context, i, k, iterable, bl);
        this.drawStatusEffectDescriptions(context, i, k, iterable);
    }

//    private void drawStatusEffectBackgrounds(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
//        int i = this.y;
//        for (StatusEffectInstance statusEffectInstance : statusEffects) {
//            if (wide) {
//                context.drawTexture(BACKGROUND_TEXTURE, x, i, 0, 166, 120, 32);
//            } else {
//                context.drawTexture(BACKGROUND_TEXTURE, x, i, 0, 198, 32, 32);
//            }
//            i += height;
//        }
//    }

    private void drawStatusEffectSprites(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
        StatusEffectSpriteManager statusEffectSpriteManager = Falsify.mc.getStatusEffectSpriteManager();
        int i = -2;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
            context.drawSprite(x + (wide ? 6 : 7), i + 7, 0, 18, 18, sprite);
            i += height;
        }
    }

    private void drawStatusEffectDescriptions(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects) {
        int i = -2;
        int max = 0;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            Text text = this.getStatusEffectDescription(statusEffectInstance);
            context.drawTextWithShadow(Falsify.mc.textRenderer, text, x + 10 + 18, i + 6, module.getTextColor().getRGB());
            Text text2 = StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f);
            max = Math.max(max, Falsify.mc.textRenderer.getWidth(text));
            context.drawTextWithShadow(Falsify.mc.textRenderer, text2, x + 10 + 18, i + 6 + 10, module.getTextColor().darker().getRGB());
            i += height;
        }
        setWidth(Math.max(32 + max, 75));
        setHeight(Math.max(i-height+20+Falsify.mc.textRenderer.fontHeight, height));
    }

    private Text getStatusEffectDescription(StatusEffectInstance statusEffect) {
        MutableText mutableText = statusEffect.getEffectType().getName().copy();
        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
        }
        return mutableText;
    }
}
