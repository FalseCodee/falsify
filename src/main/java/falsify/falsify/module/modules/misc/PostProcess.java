package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;

public class PostProcess extends Module {
    private final BooleanSetting derivativeFilter = new BooleanSetting("Derivative Filter", false);

    public PostProcess() {
        super("Post Process", "Adds Post Process Effects", false, Category.MISC, -1);
        settings.add(derivativeFilter);
    }

    @Override
    public void onEnable() {
        this.toggle();
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventRender3d)) {
        }

    }

    public boolean shouldRenderDerivativeFilter() {
        return derivativeFilter.getValue();
    }
}
