package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.credits.CreditScreen;
import falsify.falsify.gui.other.FollowerGuy;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ClientMenuScreen extends Screen {

    public final ArrayList<Clickable> buttons = new ArrayList<>();
    private Color color1 = new Color(0, 0, 0);
    private Color color2 = new Color(0, 0, 0);
    private Color color3 = new Color(0, 0, 0);
    private Color color4 = new Color(0, 0, 0);

    private LegacyIdentifier title;
    private LegacyIdentifier titleBackground;
    private final ArrayList<FollowerGuy> guys;

    private final ServerInfo warSpree = new ServerInfo("achieved", "achieved.minehut.gg", ServerInfo.ServerType.OTHER);
    public ClientMenuScreen() {
        super(Text.of("Menu Screen"));
        addButton("Singleplayer", new SelectWorldScreen(this));
        addButton("Multiplayer", new MultiplayerScreen(this));
        addButton("Settings", new OptionsScreen(this, Falsify.mc.options));
        addButton("Join Achieved", () -> ConnectScreen.connect(this, Falsify.mc, ServerAddress.parse(warSpree.address), warSpree, false));
        addButton("Credits", new CreditScreen(this));
        guys = new ArrayList<>();
    }

    @Override
    public void init() {
        super.init();
        guys.clear();
        for(int i = 0; i < (int) (0.00082*width*height); i++) {
            guys.add(new FollowerGuy(MathUtils.random(10, width-10), MathUtils.random(10, height-10)));
        }

        int padding = 1;
        for(int i = 0; i < buttons.size(); i++) {
            Clickable c = buttons.get(i);

            c.setX(width/2f);
            c.setY(height/2f+(double)(25+padding)*i);

        }
    }

    private void addButton(String name, Screen goTo) {
        addButton(name, () -> Falsify.mc.setScreen(goTo));
    }

    private void addButton(String name, Runnable runnable) {
        buttons.add(new Clickable.ButtonBuilder().pos(0, height-30).dimensions(200, 25)
                .onClick(((clickable, x, y, button) -> {
                    if(clickable.isHovering(x+clickable.getWidth()/2, y+clickable.getHeight()/2)) {
                        runnable.run();
                        return true;
                    }
                    return false;
                }))
                .onRender((clickable, context, mouseX, mouseY, delta) -> {
                    Color hover = (clickable.isHovering(mouseX+clickable.getWidth()/2, mouseY+clickable.getHeight()/2)) ? new Color(144, 144, 144, 128) : new Color(60, 60, 60, 128);
                    MatrixStack matrices = context.getMatrices();
                    matrices.push();
                    matrices.translate(clickable.getX(), clickable.getY(), 0);
                    RenderHelper.drawSmoothRect(hover.darker().darker(),context.getMatrices(), (int)(-clickable.getWidth()/2f), (int)(-clickable.getHeight()/2f), (int)(clickable.getWidth()/2f), (int)(clickable.getHeight()/2f), 7, new int[] {5, 5, 5, 5});
                    RenderHelper.drawSmoothRect(hover,context.getMatrices(),  (int)(-clickable.getWidth()/2f)+1, (int)(-clickable.getHeight()/2f)+1, (int)(clickable.getWidth()/2f)-1, (int)(clickable.getHeight()/2f)-1, 7, new int[] {5, 5, 5, 5});
                    int scale = 1;
                    matrices.scale(scale, scale, 1);
                    Falsify.fontRenderer.drawCenteredString(context, name, 0, -Falsify.fontRenderer.getStringHeight(name)/2, Color.WHITE, true);
                    matrices.pop();
                }).build());
    }

    @Override
    public void tick() {
        super.tick();
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

    private void setTitle() {
        if(this.title == null) {
            this.title = Falsify.textureCacheManager.getIdentifier("title");
        }
    }

    private void setTitleBackground() {
        if(this.titleBackground == null) {
            this.titleBackground = Falsify.textureCacheManager.getIdentifier("title_background");
        }
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        color1 = RenderHelper.colorLerp(color1, new Color(Color.HSBtoRGB((mouseY/(float)height*1.5f + 0.23f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color2 = RenderHelper.colorLerp(color2, new Color(Color.HSBtoRGB((mouseX/(float)width*0.5f  + 0.86f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color3 = RenderHelper.colorLerp(color3, new Color(Color.HSBtoRGB((mouseY/(float)height*0.8f + 0.45f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
        color4 = RenderHelper.colorLerp(color4, new Color(Color.HSBtoRGB((mouseX/(float)width*0.9f  + 0.33f), 1.00f, 0.25f)), MathUtils.clamp(0.1f*Falsify.mc.getLastFrameDuration(), 0.0f, 1.0f));
//        RenderUtils.fillCornerGradient(context,0, 0, width, height, color1.getRGB(), color2.getRGB(),color3.getRGB(), color4.getRGB());

        int size = guys.size();
        for(int i = guys.size()-1; i >=0 ;i--) {
            if(i >= guys.size()) i -= 1 + i-guys.size();
            FollowerGuy guy = guys.get(i);
            guy.color = new Color(Color.HSBtoRGB((float) ((float) (guy.getX()/(float)width*0.5f + 0.23f) / 2f + (guy.getY()/(float)height*0.5f + 0.23f) / 2f), 0.5f, 1.0f));
            boolean flag = guys.size() == size;
            guy.update(mouseX, mouseY, delta, guys, flag);
            guy.render(context, mouseX, mouseY, delta);
        }
    }

    private float sum = 0;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(title == null) {
            setTitle();
            return;
        }

        if(titleBackground == null) {
            setTitleBackground();
            return;
        }

        context.drawTexture(titleBackground, 0, 0, ((float) mouseX / width) * 160, ((float) mouseY / height) * 90, titleBackground.getWidth(), titleBackground.getHeight(), width+160, height+90);
        renderBackground(context, mouseX, mouseY, delta);
        for(Clickable button : buttons)  {
            button.render(context, mouseX, mouseY, delta);
        }

        context.getMatrices().push();
        float scale = width/800f/2f/1.25f;
        context.getMatrices().scale(scale, scale, 1);
        context.getMatrices().translate(width/2f/scale, (height/3f)/scale, 0);
        context.drawTexture(title, -title.getWidth()/2, -title.getHeight()/2, 0,0, title.getWidth(), title.getHeight(), title.getWidth(), title.getHeight());
        context.getMatrices().pop();
    }
}
