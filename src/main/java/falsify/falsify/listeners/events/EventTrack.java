package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Set;

public class EventTrack extends Event<EventTrack> {
    public EventTrack(PlayerEntity player, boolean start) {
        this.player = player;
        this.start = start;
    }

    public EventTrack(boolean start) {
        this.start = start;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public boolean isStart() {
        return start;
    }

    PlayerEntity player;
    boolean start;

}
