package falsify.falsify.listeners.events;

import falsify.falsify.listeners.Event;
import net.minecraft.network.packet.s2c.play.ChunkData;

public class EventChunkLoad extends Event<EventChunkLoad> {
    private final int x;
    private final int z;
    private final ChunkData chunkData;

    public EventChunkLoad(int x, int z, ChunkData chunkData) {
        this.x = x;
        this.z = z;
        this.chunkData = chunkData;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public ChunkData getChunkData() {
        return chunkData;
    }
}
