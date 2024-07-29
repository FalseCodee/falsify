package falsify.falsify.module.modules.misc;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.listeners.events.EventRenderScreen;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.MathUtils;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.awt.*;

public class NbtViewer extends Module {

    private String nbtData = "";
    private String nbtUnchanged = "";
    private float scrollOffset = 0;
    public NbtViewer() {
        super("NBT Viewer", "Shows the NBT of items.", false, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(mc.currentScreen instanceof HandledScreen<?> handledScreen)) return;
        if(event instanceof EventRenderScreen render && render.isPost()) {

            ItemStack itemStack = handledScreen.getScreenHandler().getCursorStack();
            //TODO: FIX
            NbtCompound nbt = null;
            if (nbt != null) {
                String newData = ChatModuleUtils.beautifyNbt(nbt).replace("\\\"", "\"").replace("§", "&");
                if(!newData.equals(nbtData)) scrollOffset = 0;
                nbtData = newData;
                nbtUnchanged = nbt.asString();
            }
            render.getDrawContext().getMatrices().push();
            render.getDrawContext().getMatrices().translate(5 * mc.getWindow().getScaledWidth() / 8.0f, scrollOffset + 10, 1);

            if(!nbtData.equals("")) {
                float width = Falsify.fontRenderer.getStringWidth(nbtData);
                float height = Falsify.fontRenderer.getStringHeight(nbtData);

                render.getDrawContext().fill(-5, -5, (int) (width + 5), (int) (height + 5), Color.BLACK.getRGB());
            }
            Falsify.fontRenderer.drawString(render.getDrawContext(), nbtData, 0, 0, Color.WHITE, true);
            render.getDrawContext().getMatrices().push();

        } else if(event instanceof EventMouse.Scroll scroll) {
            double padding = 10;
            double topClamp = 5 + padding;
            double bottomClamp = mc.getWindow().getScaledHeight() - padding*2 - 5;
            double topPoint = scrollOffset + 10;
            double bottomPoint = topPoint + Falsify.fontRenderer.getStringHeight(nbtData);
            if (bottomClamp - topClamp > bottomPoint - topPoint) return;
            double scrollDistance = MathUtils.clamp(scroll.getVertical(), bottomClamp - bottomPoint, topClamp - topPoint);

            scrollOffset += scrollDistance * 10;
        } else if(event instanceof EventMouse mouse) {
            if(mouse.button == 0 && mouse.action == 1 && mc.mouse.getX() > 5 * mc.getWindow().getWidth() / 8.0f) {
                mc.player.sendMessage(Text.of("Copied to clipboard."));
                mc.keyboard.setClipboard(nbtUnchanged);
            }
        }
    }
}
