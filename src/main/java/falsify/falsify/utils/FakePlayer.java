package falsify.falsify.utils;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import net.minecraft.client.network.OtherClientPlayerEntity;

import java.util.UUID;

public class FakePlayer extends OtherClientPlayerEntity {
    public FakePlayer() {
        super(Falsify.mc.world, new GameProfile(UUID.randomUUID(), "Legacy Client"));
    }
}
