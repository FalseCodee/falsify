package falsify.falsify.module.modules.render;

import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;

public class Fullbright extends Module {

    private final RangeSetting gamma = new RangeSetting("Gamma", 30, -100, 100, 1);

    public Fullbright() {
        super("Fullbright", "Increase default brightness.", false, Category.RENDER, -1);
        settings.add(gamma);
    }

    public Double getValue() {
        return gamma.getValue()/10d;
    }
}
