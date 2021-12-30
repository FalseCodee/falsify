package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.client.render.debug.PathfindingDebugRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.Tag;
import org.lwjgl.glfw.GLFW;

import java.util.Set;

public class DiamondGrabber extends Module {
    Timer hometimer = new Timer();
    Timer timer = new Timer();
    Timer fallback = new Timer();
    public DiamondGrabber() {
        super("Diamonds!", Category.MISC, GLFW.GLFW_KEY_N);
    }
    double[] coords = {-42, 67 ,42};

    public boolean hasDiamond(){
        assert mc.player != null;
        for(ItemStack item : mc.player.getInventory().main){
            if(item.getItem() == Items.DIAMOND){
                return true;
            }
            return false;
        }
        return false;
    }
    boolean running = false;
    @Override
    public void onEvent(Event e) {
        if(e instanceof EventUpdate){
            assert mc.player != null;
            if(hasDiamond()){
                if(timer.hasTimeElapsed(1000*11, true)){
                    mc.player.sendChatMessage("/home base");
                    hometimer.reset();
                    fallback.reset();
                }
                if(hometimer.hasTimeElapsed(1000*10, false) && timer.hasTimeElapsed(1000*2, false)){
                    hometimer.reset();
                    mc.player.sendChatMessage("/suicide");
                    fallback.reset();
                }
            } else{
                if(!mc.player.isDead()){
                    if(Math.sqrt((coords[0]-mc.player.getX())*(coords[0]-mc.player.getX())+(coords[2]-mc.player.getZ())*(coords[2]-mc.player.getZ())) >= 1.5){
                        mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[0], 0.5f));
                        mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[1], 0.5f));
                        mc.options.keyForward.setPressed(true);
                        mc.options.keySneak.setPressed(false);

                        running = true;
                    }else if(running){
                        running = false;
                        mc.options.keyForward.setPressed(false);
                    }
                    else {
                        if(fallback.hasTimeElapsed(1000*180, true)){
                            mc.player.sendChatMessage("/suicide");
                        }
                        mc.options.keySneak.setPressed(true);

                    }
                }
            }
        }

    }
}
