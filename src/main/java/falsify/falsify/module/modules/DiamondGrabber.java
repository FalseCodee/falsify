package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DiamondGrabber extends Module {
    final Timer hometimer = new Timer();
    final Timer timer = new Timer();
    final Timer fallback = new Timer();
    public DiamondGrabber() {
        super("Diamonds!", Category.MISC, GLFW.GLFW_KEY_N);
    }
    final double[] coords = {-42, 67 ,42};

    public boolean hasDiamond(){
        assert mc.player != null;
        for(ItemStack item : mc.player.getInventory().main){
            return item.getItem() == Items.DIAMOND;
        }
        return false;
    }
    boolean running = false;
    @Override
    public void onEvent(Event event) {
        if(event instanceof EventRender){
            assert mc.player != null;
            if(hasDiamond()){
                if(timer.hasTimeElapsed(1000*11, true)){
                    mc.player.sendMessage(Text.of("/home base"));
                    hometimer.reset();
                    fallback.reset();
                }
                if(hometimer.hasTimeElapsed(1000*10, false) && timer.hasTimeElapsed(1000*2, false)){
                    hometimer.reset();
                    mc.player.sendMessage(Text.of("/suicide"));
                    fallback.reset();
                }
            } else{
                if(!mc.player.isDead()){
                    if(Math.sqrt((coords[0]-mc.player.getX())*(coords[0]-mc.player.getX())+(coords[2]-mc.player.getZ())*(coords[2]-mc.player.getZ())) >= 1.5){
                        mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[0], 0.1f));
                        mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(coords[0],coords[1],coords[2])[1], 0.1f));
                        mc.options.forwardKey.setPressed(true);
                        mc.options.sneakKey.setPressed(false);

                        running = true;
                    }else if(running){
                        running = false;
                        mc.options.forwardKey.setPressed(false);
                    }
                    else {
                        if(fallback.hasTimeElapsed(1000*180, true)){
                            mc.player.sendMessage(Text.of("/suicide"));
                        }
                        mc.options.sneakKey.setPressed(true);

                    }
                }
            }
        }

    }
}
