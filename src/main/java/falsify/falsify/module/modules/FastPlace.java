package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;

import java.text.DecimalFormat;

public class FastPlace extends Module {
    public FastPlace() {
        super("Fast Place", Category.MOVEMENT, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
       if(event instanceof EventUpdate) {
           ((MixinMinecraft)mc).setCoolDown(0);
       }
    }

    @Override
    public void onDisable() {
        ((MixinMinecraft)mc).setCoolDown(4);
    }
}
