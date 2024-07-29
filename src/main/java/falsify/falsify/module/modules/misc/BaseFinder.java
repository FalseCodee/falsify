package falsify.falsify.module.modules.misc;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventChunkLoad;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.utils.DiscordWebhookBuilder;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.NetworkUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Arrays;

public class BaseFinder extends Module {
    private StringBuilder stringBuilder;
    private final Timer timer = new Timer();
    private final BooleanSetting containersOnly = new BooleanSetting("Containers only", true);
    public BaseFinder() {
        super("Base Finder", "Finds bases using block entities", true, Category.MISC, -1);
        settings.add(containersOnly);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(!timer.hasTimeElapsed(5000, true)) return;

            if(stringBuilder == null || stringBuilder.toString().length() <= "Block Entities found at:  \n".length()) return;
            DiscordWebhookBuilder builder = new DiscordWebhookBuilder();
            builder.username("Base Chunk Located!");
            DiscordWebhookBuilder.EmbedBuilder embed = builder.embed();
            embed.color(0xFF4444)
                    .title("Base flagged:");

            embed.description(stringBuilder.toString()).build();
            embed.footer().text("lets get hunting").build();

            stringBuilder = null;
            new FalseRunnable() {
                @Override
                public void run() {

                    try {
                        NetworkUtils.postRequest(Falsify.DISCORD_WEBHOOK_URL, builder.build().toString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }.runTaskAsync();
        }
        if(event instanceof EventChunkLoad eventChunkLoad) {

            ChunkData data = eventChunkLoad.getChunkData();
            if(stringBuilder == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Block Entities found at:  \n");
            }
            data.getBlockEntities(eventChunkLoad.getX(), eventChunkLoad.getZ()).accept((pos, type, nbt) -> {
                Identifier id = BlockEntityType.getId(type);
                assert id != null;
                String idName = id.getPath();
                if(containersOnly.getValue()) {
                    String[] containers = {"chest", "trapped_chest", "ender_chest", "hopper", "shulker_box", "barrel"};
                    if(!Arrays.asList(containers).contains(idName)) return;
                }

                stringBuilder.append(pos.toShortString())
                        .append(" (")
                        .append(idName)
                        .append(")")
                        .append("\n");

            });
        }
    }
}
