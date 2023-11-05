package falsify.falsify.module.modules.player;

import com.mojang.authlib.GameProfile;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMovementTick;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.FakePlayer;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class Freecam extends Module {
    private FakePlayer fakePlayer;
    private final Timer timer = new Timer();
    public Freecam() {
        super("Freecam", "Fly out of your body", true, Category.PLAYER, -1);
    }

    @Override
    public void onEnable() {
        if(toggled &&(mc.player == null || mc.world == null)) {
            this.toggle();
            return;
        }
        fakePlayer = new FakePlayer();
        fakePlayer.copyPositionAndRotation(mc.player);

        fakePlayer.setHeadYaw(mc.player.getHeadYaw());
        fakePlayer.setBodyYaw(mc.player.bodyYaw);
        mc.world.addEntity(fakePlayer);
        if(mc.player.isSprinting()) mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        mc.chunkCullingEnabled = false;
    }

    @Override
    public void onDisable() {
        if(mc.player == null) return;
        mc.player.copyPositionAndRotation(fakePlayer);

        mc.player.setHeadYaw(fakePlayer.getHeadYaw());
        mc.player.setBodyYaw(fakePlayer.bodyYaw);
        mc.player.setVelocity(0, 0, 0);
        mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
        mc.player.getAbilities().flying = false;
        mc.player.noClip = false;
        mc.chunkCullingEnabled = mc.player.isSpectator();

    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketSend e && mc.world != null) {
            Packet<?> p = e.getPacket();
            if(p instanceof PlayerMoveC2SPacket.PositionAndOnGround packet) {
                if(!(packet.getX(0.0) == fakePlayer.getX() && packet.getY(0.0) == fakePlayer.getY() && packet.getZ(0.0) == fakePlayer.getZ())) {
                    e.setCancelled(true);
                }
            }
            else if(p instanceof PlayerMoveC2SPacket) e.setCancelled(true);
            else if(p instanceof PlayerInputC2SPacket) e.setCancelled(true);
            else if(p instanceof PlayerActionC2SPacket) e.setCancelled(true);
            else if(p instanceof PlayerInteractEntityC2SPacket) e.setCancelled(true);
            else if(p instanceof PlayerInteractBlockC2SPacket) e.setCancelled(true);
            else if(p instanceof PlayerInteractItemC2SPacket) e.setCancelled(true);
            else if(p instanceof HandSwingC2SPacket) e.setCancelled(true);
            else if(p instanceof UpdatePlayerAbilitiesC2SPacket) e.setCancelled(true);
            else if(p instanceof ClientCommandC2SPacket packet && packet.getMode() != ClientCommandC2SPacket.Mode.STOP_SPRINTING) e.setCancelled(true);
        }
        if(event instanceof EventMovementTick && event.isPre()) {
            mc.player.noClip = true;
            double[] speed = MathUtils.directionSpeed(1);
            double vertSpeed = MathUtils.getVerticalMov() * 1;
            mc.player.setVelocity(speed[0], vertSpeed, speed[1]);
            fakePlayer.tickMovement();
            if(timer.hasTimeElapsed(50, true))
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(fakePlayer.getX(), fakePlayer.getY(), fakePlayer.getZ(), fakePlayer.isOnGround()));
        } else if (event instanceof EventUpdate) {
            fakePlayer.tick();
        }
    }
}
