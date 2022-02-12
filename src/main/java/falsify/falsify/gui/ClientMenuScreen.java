package falsify.falsify.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClientMenuScreen extends Screen {

    private final Screen parent;

    protected ClientMenuScreen(Screen parent) {
        super(Text.of("Menu Screen"));
        this.parent = parent;
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
