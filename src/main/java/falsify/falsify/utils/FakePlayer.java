package falsify.falsify.utils;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;

import java.util.UUID;

public class FakePlayer extends OtherClientPlayerEntity {
    public FakePlayer() {
        super(Falsify.mc.world, new GameProfile(UUID.randomUUID(), Falsify.mc.player.getGameProfile().getName()));
        DataTracker playerTracker = Falsify.mc.player.getDataTracker();
        DataTracker fakePlayerTracker = this.getDataTracker();
        Byte model = playerTracker.get(PLAYER_MODEL_PARTS);
        fakePlayerTracker.set(PLAYER_MODEL_PARTS, model);
    }

    public void copyPositionTo(LivingEntity entity) {
        entity.copyPositionAndRotation(this);

        entity.setHeadYaw(this.getHeadYaw());
        entity.setBodyYaw(this.bodyYaw);
        entity.setVelocity(this.getVelocity());
    }
}
