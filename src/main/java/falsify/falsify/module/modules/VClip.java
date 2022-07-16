package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class VClip extends Module {

    private final BooleanSetting velocity = new BooleanSetting("Velocity", true);

    public VClip() {
        super("V-Clip", Category.MOVEMENT, GLFW.GLFW_KEY_0);
        settings.add(velocity);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend eventPacketSend) {
            if(eventPacketSend.packet instanceof ChatMessageC2SPacket packet) {
                String[] args = packet.getChatMessage().split(" ");
                if(args[0].equalsIgnoreCase(".clip") && args.length == 4) {
                    try{
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);

                        if(velocity.getValue()) mc.player.setVelocity(new Vec3d(x/10, y/10, z/10));
                        mc.player.setPosition(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
                    } catch (NumberFormatException exception) {
                        mc.player.sendMessage(Text.of("Usage: .clip <number> <number> <number>"), false);
                    }
                    eventPacketSend.setCancelled(true);
                }
            }
        }
    }
}
