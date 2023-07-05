package falsify.falsify.gui.clickgui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.primatives.Tab;
import falsify.falsify.gui.editor.EditGUI;
import falsify.falsify.module.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {

    private final ArrayList<Tab> tabs = new ArrayList<>();
    private final Clickable editButton;
    public ClickGUI() {
        super(Text.of(""));
        int i = 0;
        editButton = new Clickable(this.width/2.0, 3*this.height/4.0 + 5, 50, 50) {
            @Override
            public boolean handleClick(double x, double y, int button) {
                if(isHovering(x, y)) {
                    Falsify.mc.setScreen(new EditGUI());
                    return true;
                }
                return false;
            }

            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                drawSmoothRect(new Color(89, 89, 89, 255), context.getMatrices(), (float) x, (float) y, (float) (x + width), (float) (y + height),25, new int[] {10,10,10,10});
                //context.getMatrices().push();
                //.getMatrices().translate(0.0, 0.0, -0.03);
                context.drawCenteredTextWithShadow(Falsify.mc.textRenderer, "Edit", (int) x + (int) width/2, (int) y + (int) height/2 - Falsify.mc.textRenderer.fontHeight/2, 0xffffff);
                //context.getMatrices().pop();

            }
        };
        for(Category category : Category.values()) {
            tabs.add(new Tab(category, 10 + 120 * i, 10));
            i++;
        }
        Falsify.configManager.loadClickGui(tabs);
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
        if(editButton.handleClick(mouseX, mouseY, button)) return true;


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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        editButton.render(context, mouseX, mouseY, delta);
        for(int i = tabs.size()-1; i >= 0; i--) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0, 0.0, 0.03*tabs.size()-0.03*i);
            tabs.get(i).render(context, mouseX, mouseY, delta);
            context.getMatrices().pop();
        }
    }

    public List<Tab> getTabs() {
        return this.tabs;
    }
}
