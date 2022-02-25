package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import org.lwjgl.glfw.GLFW;

public class Flight extends Module {
    public Flight() {
        super("Flight", Category.MISC, GLFW.GLFW_KEY_V);
    }
    @Override
    public void onEnable() {
        System.out.println(this.name + ": Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println(this.name + ": Disabled!");
        assert mc.player != null;
        mc.player.getAbilities().flying = false;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate){
            assert mc.player != null;
            mc.player.getAbilities().flying = true;
        }
    }
}
