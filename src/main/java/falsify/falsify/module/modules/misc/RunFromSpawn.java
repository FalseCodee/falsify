package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class RunFromSpawn  extends Module {
    public RunFromSpawn() {
        super("RUN!!!!", Category.MOVEMENT, -1);
    }
    final Timer timer = new Timer();

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate){
            if(mc.player.isDead()){
                mc.player.requestRespawn();
            }
            mc.options.forwardKey.setPressed(true);
            mc.player.setSprinting(mc.player.input.movementForward > 0 && mc.player.input.movementSideways != 0 || mc.player.input.movementForward > 0 && !mc.player.isSneaking());
            mc.options.jumpKey.setPressed(mc.player.isOnGround());
            if(mc.player.getHungerManager().getFoodLevel() <= 6){
                if(timer.hasTimeElapsed(10000, true)){
                    mc.player.sendMessage(Text.of("/sethome travel"));
                    mc.player.sendMessage(Text.of("/suicide"));
                }

            }
        }
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
    }
}
