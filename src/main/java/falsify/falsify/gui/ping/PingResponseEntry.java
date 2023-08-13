package falsify.falsify.gui.ping;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.utils.Animation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ServerInfo;

import java.awt.*;

public class PingResponseEntry extends Clickable {

    private final ServerInfo serverInfo;
    private final Clickable copyAddress;
    private final Clickable checkPlayers;

    public boolean isActive = false;
    private final Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);

    public PingResponseEntry(ServerInfo serverInfo, double x, double y) {
        super(x, y, 400, 46);
        this.serverInfo = serverInfo;
        this.copyAddress = new Clickable(5, 4, Falsify.mc.textRenderer.getWidth(serverInfo.address) + 10, 18) {

            final Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);
            @Override
            public boolean handleClick(double x, double y, int button) {
                if(isHovering(x, y)) {
                    Falsify.mc.keyboard.setClipboard(serverInfo.address);
                }
                return false;
            }

            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                if(isHovering(mouseX, mouseY)) fade.rise(); else fade.lower();
                context.getMatrices().push();
                context.getMatrices().translate(this.x, this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), context.getMatrices(), 0, 0, (float) width, (float) height, 3, new int[] {5, 5, 5, 5});
                context.getMatrices().translate(mouseX-this.x, mouseY-this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), context.getMatrices(), 0, -15, (float) 50, (float) 0, 3, new int[] {5, 5, 5, 5});
                if(fade.getProgress() != 0) context.drawTextWithShadow(Falsify.mc.textRenderer, "Copy?", 10, -12, fade.color(new Color(255, 255, 255, 0), new Color(255, 255, 255, 254)).getRGB());
                fade.tick();
                context.getMatrices().pop();
            }
        };

        this.checkPlayers = new Clickable((width - 10 - Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString())-5), 4, Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString()) + 10, 18) {

            final Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);
            @Override
            public boolean handleClick(double x, double y, int button) {
                if(isHovering(x, y)) {
                    Falsify.mc.keyboard.setClipboard(serverInfo.address);
                }
                return false;
            }

            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                if(isHovering(mouseX, mouseY)) fade.rise(); else fade.lower();
                context.getMatrices().push();
                context.getMatrices().translate(this.x, this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), context.getMatrices(), 0, 0, (float) width, (float) height, 3, new int[] {5, 5, 5, 5});
                if(fade.getProgress() != 0){
                    int maxLen = 0;
                    for(GameProfile profile : serverInfo.players.sample()) {
                        int len = Falsify.mc.textRenderer.getWidth(profile.getName());
                        if(len > maxLen) maxLen = len;
                    }
                    context.getMatrices().translate(mouseX-this.x, mouseY-this.y, 0);
                    drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), context.getMatrices(), 0, 0, (float) maxLen+20, (float) 18*serverInfo.players.sample().size(), 3, new int[] {5, 5, 5, 5});
                    for(int i = 0; i < serverInfo.players.sample().size(); i++) {
                        context.drawTextWithShadow(Falsify.mc.textRenderer, serverInfo.players.sample().get(i).getName(), 10, 3 + 18*i, fade.color(new Color(255, 255, 255, 0), new Color(255, 255, 255, 254)).getRGB());
                    }
                }
                fade.tick();
                context.getMatrices().pop();
            }
        };
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(copyAddress.handleClick(x-this.x, y-this.y, button)) return true;
        if(isHovering(x,y)) {
            this.isActive = !this.isActive;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(isHovering(mouseX, mouseY) || isActive) fade.rise(); else fade.lower();

        mouseX-=this.x;
        mouseY-=this.y;
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 0);
        context.drawBorder(0, 0, (int) width, (int) height, Color.WHITE.getRGB());
        drawRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, (isActive) ? 150 : 100)), context.getMatrices(), 0, 0, (int) width, (int) height);
        checkPlayers.render(context,mouseX, mouseY, delta);
        copyAddress.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(Falsify.mc.textRenderer, serverInfo.address, 10, 10, Color.WHITE.getRGB());
        context.drawTextWithShadow(Falsify.mc.textRenderer, "Online Mode: " + serverInfo.online, 10, 10 + 18, Color.WHITE.getRGB());
        context.drawTextWithShadow(Falsify.mc.textRenderer, "Players: " + serverInfo.playerCountLabel.getString(), (int) (width - 10 - Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString())), 10, Color.WHITE.getRGB());
        context.drawTextWithShadow(Falsify.mc.textRenderer, "Version: " + serverInfo.version.getString(), (int) (width - 10 - Falsify.mc.textRenderer.getWidth("Version: " + serverInfo.version.getString())), 10 + 18, Color.WHITE.getRGB());

        context.getMatrices().pop();
        fade.tick();
    }
}
