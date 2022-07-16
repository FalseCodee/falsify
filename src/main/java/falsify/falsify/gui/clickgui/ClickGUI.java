package falsify.falsify.gui.clickgui;

import falsify.falsify.gui.clickgui.primatives.Tab;
import falsify.falsify.module.Category;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ClickGUI extends Screen {

    private final ArrayList<Tab> tabs = new ArrayList<>();
    public ClickGUI() {
        super(Text.of(""));
        int i = 0;
        for(Category category : Category.values()) {
            tabs.add(new Tab(category, 10 + 120 * i, 10));
            i++;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(Tab tab : tabs) {
            if(tab.handleClick(mouseX, mouseY, button)) {
                tabs.remove(tab);
                tabs.add(0, tab);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(Tab tab : tabs) {
            if(tab.isDragging()) tab.setDragging(false);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(Tab tab : tabs) {
            if(tab.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(int i = tabs.size()-1; i >= 0; i--) {
            tabs.get(i).render(matrices, mouseX, mouseY, delta);
        }
    }
}
