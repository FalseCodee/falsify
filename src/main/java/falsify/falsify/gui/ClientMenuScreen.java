package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.other.FollowerGuy;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class ClientMenuScreen extends Screen {

    public final ArrayList<Clickable> buttons = new ArrayList<>();
    private Color color1 = new Color(255, 255, 255);
    private Color color2 = new Color(255, 255, 255);
    private Color color3 = new Color(255, 255, 255);
    private Color color4 = new Color(255, 255, 255);
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
        for(int i = 0; i < (int) (0.00082*width*height); i++) {
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
                   Color hover = (clickable.isHovering(mouseX, mouseY)) ? new Color(144, 144, 144, 75) : new Color(60, 60, 60,75);
                   RenderHelper.drawSmoothRect(hover.darker().darker(),context.getMatrices(), (int)clickable.getX()+1, (int)clickable.getY()+1, (int)(clickable.getX() + clickable.getWidth()-1), (int)(clickable.getY() + clickable.getHeight()-1), 7, new int[] {5, 5, 5, 5});
                   RenderHelper.drawSmoothRect(hover,context.getMatrices(), (int)clickable.getX()+2, (int)clickable.getY()+2, (int)(clickable.getX() + clickable.getWidth()-2), (int)(clickable.getY() + clickable.getHeight()-2), 7, new int[] {5, 5, 5, 5});
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
        color1 = RenderHelper.colorLerp(color1, new Color(Color.HSBtoRGB((mouseY/(float)height*1.5f + 0.23f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color2 = RenderHelper.colorLerp(color2, new Color(Color.HSBtoRGB((mouseX/(float)width*0.5f  + 0.86f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color3 = RenderHelper.colorLerp(color3, new Color(Color.HSBtoRGB((mouseY/(float)height*0.8f + 0.45f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color4 = RenderHelper.colorLerp(color4, new Color(Color.HSBtoRGB((mouseX/(float)width*0.9f  + 0.33f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        RenderUtils.fillCornerGradient(context,0, 0, width, height, color1.getRGB(), color2.getRGB(),color3.getRGB(), color4.getRGB());
        int size = guys.size();
        for(int i = guys.size()-1; i >=0 ;i--) {
            if(i >= guys.size()) i -= 1 + i-guys.size();
            FollowerGuy guy = guys.get(i);
            boolean flag = guys.size() == size;
            guy.update(mouseX, mouseY, delta, guys, flag);
            guy.render(context, mouseX, mouseY, delta);
        }

        for(Clickable button : buttons)  {
            button.render(context, mouseX, mouseY, delta);
        }

        context.getMatrices().push();
        int scale = 3;
        context.getMatrices().scale(scale, scale, 1);
        context.getMatrices().translate(width/2f/scale, height/2f/scale, 0);
        int titleWidth = textRenderer.getWidth(Falsify.title);
        int titleHeight = textRenderer.fontHeight;
        int padding = 7;
        RenderHelper.drawSmoothRect(new Color(64,64,64, 40), context.getMatrices(), -titleWidth/2f - padding, -titleHeight/2f - padding,titleWidth/2f + padding, titleHeight/2f + padding, 5, new int[] {5,5,5,5});
        context.drawCenteredTextWithShadow(textRenderer, Falsify.title, 0, -titleHeight/2, -1);
        context.getMatrices().pop();
    }
}
