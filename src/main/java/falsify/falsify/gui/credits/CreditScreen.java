package falsify.falsify.gui.credits;

import falsify.falsify.Falsify;
import falsify.falsify.gui.ClientMenuScreen;
import falsify.falsify.utils.playerdata.PlayerData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class CreditScreen extends Screen {
    private final ArrayList<PlayerDataEntry> entries = new ArrayList<>();
    private final ClientMenuScreen parent;
    public CreditScreen(ClientMenuScreen parent) {
        super(Text.of(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        parent.init(Falsify.mc, width, height);
        entries.clear();
        if(Falsify.playerDataManager.getDataList() == null || Falsify.playerDataManager.getDataList().size() == 0) return;
        for(int i = 0; i < Falsify.playerDataManager.getDataList().size(); i++) {
            PlayerData data = Falsify.playerDataManager.getDataList().get(i);
            double padding = 10;
            double x = 100 + (200*(3*height/300f/4f) + padding) * i;
            double y = height/2f-150;
            entries.add(new PlayerDataEntry(data, x, y));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for(PlayerDataEntry entry : entries) {
            entry.setX(entry.getX()+amount*25);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public void close() {
        Falsify.mc.setScreen(parent);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        parent.renderBackground(context, mouseX, mouseY, delta);
        entries.forEach(playerDataEntry -> playerDataEntry.render(context, mouseX, mouseY, delta));
    }
}
