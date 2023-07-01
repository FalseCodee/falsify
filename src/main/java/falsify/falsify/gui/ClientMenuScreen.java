package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.primatives.Animation;
import falsify.falsify.gui.other.FollowerGuy;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class ClientMenuScreen extends Screen {

    public final ArrayList<Clickable> buttons = new ArrayList<>();
    private Color colorStart = new Color(255, 255, 255);
    private Color colorEnd = new Color(255, 255, 255);
    private ArrayList<FollowerGuy> guys;
    public ClientMenuScreen() {
        super(Text.of("Menu Screen"));
        addButton("Singleplayer", new SelectWorldScreen(this));
        addButton("Multiplayer", new MultiplayerScreen(this));
        addButton("Settings", new OptionsScreen(this, Falsify.mc.options));
        guys = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();
        guys.clear();
        for(int i = 0; i < (int) (0.00065*width*height); i++) {
            guys.add(new FollowerGuy(MathUtils.random(10, width-10), MathUtils.random(10, height-10)));
        }

        int padding = 5;
        for(int i = 0; i < buttons.size(); i++) {
            Clickable c = buttons.get(i);

            c.setX(width/(double)buttons.size()*i + ((i == 0) ? padding : padding/2.0));
            c.setWidth(width/(double)buttons.size()-padding*2);
            c.setY(height-c.getHeight() - padding);

        }
    }

    private void addButton(String name, Screen goTo) {
        buttons.add(new Clickable.ButtonBuilder().pos(0, height-30).dimensions(100, 75)
               .onClick(((clickable, x, y, button) -> {
                   if(clickable.isHovering(x, y)) {
                       Falsify.mc.setScreen(goTo);
                       return true;
                   }
                   return false;
               }))
               .onRender((clickable, context, mouseX, mouseY, delta) -> {
                   Color hover = (clickable.isHovering(mouseX, mouseY)) ? new Color(144, 144, 144) : new Color(60, 60, 60);
                   RenderHelper.drawSmoothRect(hover.darker().darker(),context.getMatrices(), (int)clickable.getX()+1, (int)clickable.getY()+1, (int)(clickable.getX() + clickable.getWidth()-1), (int)(clickable.getY() + clickable.getHeight()-1), 3, new int[] {5, 5, 5, 5});
                   RenderHelper.drawSmoothRect(hover,context.getMatrices(), (int)clickable.getX()+2, (int)clickable.getY()+2, (int)(clickable.getX() + clickable.getWidth()-2), (int)(clickable.getY() + clickable.getHeight()-2), 3, new int[] {5, 5, 5, 5});
                   context.drawCenteredTextWithShadow(textRenderer, Text.of(name), (int)(clickable.getX() + clickable.getWidth()/2), (int)(clickable.getY() + clickable.getHeight()/2), Color.WHITE.getRGB());
               }).build());
    }

    @Override
    public void tick() {
        super.tick();
//        int padding = 5;
//        for(int i = 0; i < buttons.size(); i++) {
//            Clickable c = buttons.get(i);
//
//            c.setX(width/(double)buttons.size()*i + padding*(i+1));
//            c.setWidth(width/(double)buttons.size()-padding*2);
//            c.setY(height-c.getHeight() - padding);
//
//        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(Clickable clickable : buttons) {
            if(clickable.handleClick(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        colorStart = RenderHelper.colorLerp(colorStart, new Color(Color.HSBtoRGB(mouseY/(float)height + 0.5f, 0.5f, 1.0f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        colorEnd = RenderHelper.colorLerp(colorEnd, new Color(Color.HSBtoRGB(mouseX/(float)width, 0.5f, 1.0f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        context.fillGradient(0, 0, width, height, colorStart.getRGB(), colorEnd.getRGB());
        for(Clickable button : buttons)  {
            button.render(context, mouseX, mouseY, delta);
        }

        for(FollowerGuy guy : guys) {
            guy.update(mouseX, mouseY, delta, guys);
            guy.render(context, mouseX, mouseY, delta);
        }

        context.getMatrices().push();
        context.getMatrices().scale(3, 3, 1);
        context.drawCenteredTextWithShadow(textRenderer, "Legacy Client", width/2/3, height/2/3, -1);
        context.getMatrices().pop();
    }
}
