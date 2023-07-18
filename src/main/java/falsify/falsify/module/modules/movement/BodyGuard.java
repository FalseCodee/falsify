package falsify.falsify.module.modules.movement;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.AimbotTarget;
import falsify.falsify.utils.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BodyGuard extends Module {
    private final RangeSetting range = new RangeSetting("Running", 1, 0.1, 10, 0.1);
    private final ModeSetting location = new ModeSetting("Location", "Front", "Front", "Back", "Left", "Right");

    AimbotTarget target = null;
    Vec3d targetPos = null;

    String targetName = "FalseCode";
    public BodyGuard() {
        super("BodyGuard", "Follows a target. Requires Trajectories to be enabled.", true, Category.MOVEMENT, -1);
        settings.add(range);
        settings.add(location);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(mc.world == null) return;
       if(event instanceof EventUpdate) {
           if(target == null) {
               ArrayList<Entity> rendered = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId()))).collect(Collectors.toCollection(Lists::newArrayList));
               Entity entity = rendered.stream().filter(entity1 -> entity1 instanceof PlayerEntity && ((PlayerEntity) entity1).getGameProfile().getName().equalsIgnoreCase(targetName)).findFirst().orElse(null);
               if (entity == null) return;
               target = new AimbotTarget((LivingEntity) entity);
           }
           targetPos = getTargetPos(location.getMode());

           Trajectories.target = new AimbotTarget(targetPos);
       }

        if(event instanceof EventPacketSend packetSend) {
            if(packetSend.getPacket() instanceof ChatMessageC2SPacket packet) {
                if(packet.chatMessage().toLowerCase().startsWith(".target")) {
                    packetSend.setCancelled(true);
                    if(!packet.chatMessage().contains(" ")) {
                        mc.player.sendMessage(Text.of("Enter a player."));
                        return;
                    }
                    String user = packet.chatMessage().substring(packet.chatMessage().indexOf(" ")).trim();
                    ArrayList<Entity> rendered = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId()))).collect(Collectors.toCollection(Lists::newArrayList));
                    Entity entity = rendered.stream().filter(entity1 -> entity1 instanceof PlayerEntity && ((PlayerEntity) entity1).getGameProfile().getName().equalsIgnoreCase(user)).findFirst().orElse(null);
                    if(entity == null) {
                        mc.player.sendMessage(Text.of("Player not found."));
                    } else {
                        mc.player.sendMessage(Text.of("Target set to " + user));
                    }
                    target = new AimbotTarget((LivingEntity) entity);
                    targetName = user;
                }
            }
        }
    }

    private Vec3d getTargetPos(String mode) {
        double yaw = target.getEntity().getHeadYaw() + 90;
        Vec3d dir = MathUtils.pitchYawToVector3d(0, yaw);
        switch (mode) {
            case "Back" -> dir = dir.rotateY((float) Math.PI);
            case "Left" -> dir = dir.rotateY((float) (-Math.PI/2));
            case "Right" -> dir = dir.rotateY((float) (Math.PI/2));
        }
        return target.getEntity().getPos().add(dir.normalize().multiply(range.getValue()));
    }
}
