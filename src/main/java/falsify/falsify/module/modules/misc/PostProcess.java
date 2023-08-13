package falsify.falsify.module.modules.misc;

import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;

public class PostProcess extends Module {
    private final BooleanSetting blurBoxes = new BooleanSetting("Blur Boxes", true);
    private final BooleanSetting glow = new BooleanSetting("Glow", true);
    private final BooleanSetting glowOutline = new BooleanSetting("Glow Outlines", true);
    private final RangeSetting radius = new RangeSetting("Radius", 15, 1, 100, 1);

    public PostProcess() {
        super("Post Process", "Adds Post Process Effects", false, Category.MISC, -1);
        settings.add(radius);
        settings.add(glow);
        settings.add(glowOutline);
        settings.add(blurBoxes);
    }

    public boolean shouldBlurBoxes() {
        return blurBoxes.getValue();
    }

    public boolean shouldGlow() {
        return glow.getValue();
    }

    public boolean shouldGlowOutline() {
        return glowOutline.getValue();
    }

    public float getRadius() {
        return radius.getValue().floatValue();
    }
}
