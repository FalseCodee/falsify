package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoFish extends Module {
    public AutoFish() {
        super("Auto Fish", Category.PLAYER, GLFW.GLFW_KEY_M);
    }

    Timer timer = new Timer();
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacketRecieve) {
            if (((EventPacketRecieve) event).getPacket() instanceof PlaySoundS2CPacket) {
                if (((PlaySoundS2CPacket) ((EventPacketRecieve) event).getPacket()).getSound().equals(
                        SoundEvents.ENTITY_FISHING_BOBBER_SPLASH)) {
                    if (timer.hasTimeElapsed(1000, true)) {
                        mc.options.keyUse.setPressed(true);
                    }
                } else {
                    mc.options.keyUse.setPressed(false);
                }
            }
        }
        if (event instanceof EventUpdate) {
            if (mc.player.getMainHandStack().getItem().equals(Items.FISHING_ROD)) {
                if (mc.player.fishHook == null) {
                    if (timer.hasTimeElapsed(1000, true)) {
                        mc.options.keyUse.setPressed(true);
                    }
                } else {
                    mc.options.keyUse.setPressed(false);
                }
            }
        }
    }
}
