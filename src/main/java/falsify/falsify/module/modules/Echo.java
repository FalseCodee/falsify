package falsify.falsify.module.modules;

import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class Echo extends Module {
    public Echo() {
        super("Echo", Category.MISC, GLFW.GLFW_KEY_H);
    }

    @Override
    public void onEnable() {
        mc.getNetworkHandler().getDataQueryHandler().queryBlockNbt(new BlockPos(-8177, 50, -2489), (nbtCompound) -> {
           if(nbtCompound == null){
               mc.inGameHud.addChatMessage(MessageType.SYSTEM, Text.of("Nothing found."), mc.player.getUuid());
           } else {
               mc.inGameHud.addChatMessage(MessageType.SYSTEM, Text.of("WORKS"), mc.player.getUuid());

           }
        });
        //this.toggle();
    }
}
