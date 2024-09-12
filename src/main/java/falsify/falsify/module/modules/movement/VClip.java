package falsify.falsify.module.modules.movement;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.PlayerUtils;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

public class VClip extends Module {

    private final BooleanSetting velocity = new BooleanSetting("Velocity", true);
    private final RangeSetting blocksPerTp = new RangeSetting("Blocks per Teleport", 150,10,300,10);

    public VClip() {
        super("V-Clip", ".clip x y z", true, Category.MOVEMENT, -1);
        settings.add(velocity);
        settings.add(blocksPerTp);
        blocksPerTp.setChangedConsumer((d) -> PlayerUtils.TP_EXPLOIT_MAX_RANGE = d.intValue());
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend eventPacketSend) {
            if(eventPacketSend.packet instanceof ChatMessageC2SPacket packet) {
                String[] args = packet.chatMessage().split(" ");
                if(args[0].equalsIgnoreCase(".clip") && args.length == 4) {
                    try{
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        Vec3d pos = new Vec3d(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
                        if(velocity.getValue()) mc.player.setVelocity(new Vec3d(x/10, y/10, z/10));
                        PlayerUtils.tpExploit(pos);
                    } catch (NumberFormatException exception) {
                        mc.player.sendMessage(Text.of("Usage: .clip <number> <number> <number>"), false);
                    }
                    eventPacketSend.setCancelled(true);
                } else if(args[0].equalsIgnoreCase(".up")) {
                    event.setCancelled(true);
                    Vec3d safeLocationAbove = PlayerUtils.safeLocationAboveBlock(mc.world, mc.player.getBlockPos());

                    if(safeLocationAbove == null) {
                        mc.player.sendMessage(Text.of("No suitable location found."), false);
                        return;
                    }

                    PlayerUtils.tpExploit(safeLocationAbove);
                } else if(args[0].equalsIgnoreCase(".down")) {
                    event.setCancelled(true);
                    Vec3d safeLocationBelow = PlayerUtils.safeLocationBelowBlock(mc.world, mc.player.getBlockPos());

                    if(safeLocationBelow == null) {
                        mc.player.sendMessage(Text.of("No suitable location found."), false);
                        return;
                    }

                    PlayerUtils.tpExploit(safeLocationBelow);
                } else if(args[0].equalsIgnoreCase(".tp") && args.length == 4) {
                    try {
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        Vec3d pos = new Vec3d(x, y, z);
                        CompletableFuture<Void> a = PlayerUtils.tpExploitWithPathfinding(mc.player.getPos(), pos);
                    } catch (NumberFormatException exception) {
                        mc.player.sendMessage(Text.of("Usage: .tp <number> <number> <number>"), false);
                    }
                    eventPacketSend.setCancelled(true);
                } else if(args[0].equalsIgnoreCase(".cancel")) {
                    FalseRunnable.nextTick.clear();
                }
            }
        }
    }
}
