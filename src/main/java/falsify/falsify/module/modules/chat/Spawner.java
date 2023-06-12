package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.scanner.NbtCollector;
import org.lwjgl.glfw.GLFW;

public class Spawner extends Module {
    public Spawner() {
        super("Spawner", Category.MISC, GLFW.GLFW_KEY_APOSTROPHE);
    }

    @Override
    public void onEnable() {
        ItemStack itemStack = new ItemStack(Items.SPAWNER);
        ChatModuleUtils.sendMessage("/setblock ~ ~0 ~ minecraft:spawner{SpawnCount:100,MaxNearbyEntities:10000,SpawnRange:100,Delay:1,MinSpawnDelay:1,MaxSpawnDelay:1,RequiredPlayerRange:100,SpawnData:{entity:{id:falling_block,Time:1,BlockState:{Name:spawner},TileEntityData:{SpawnCount:100,MaxNearbyEntities:10000,SpawnRange:100,Delay:1,MinSpawnDelay:1,MaxSpawnDelay:1,RequiredPlayerRange:100,SpawnData:{entity:{id:falling_block,Time:1,BlockState:{Name:spawner},TileEntityData:{SpawnCount:5,MaxNearbyEntities:10000,SpawnRange:100,Delay:100,MinSpawnDelay:100,MaxSpawnDelay:100,RequiredPlayerRange:100,SpawnData:{entity:{id:tnt,Fuse:20}}}}}}}}} replace", false);
        this.toggle();
    }
}
