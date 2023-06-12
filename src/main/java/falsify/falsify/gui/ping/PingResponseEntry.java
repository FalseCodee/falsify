package falsify.falsify.gui.ping;

import com.mojang.authlib.GameProfile;
import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.clickgui.primatives.Animation;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class PingResponseEntry extends Clickable {

    private final ServerInfo serverInfo;
    private Clickable copyAddress;
    private Clickable checkPlayers;

    public boolean isActive = false;
    Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);

    public PingResponseEntry(ServerInfo serverInfo, double x, double y) {
        super(x, y, 400, 46);
        this.serverInfo = serverInfo;
        this.copyAddress = new Clickable(5, 4, Falsify.mc.textRenderer.getWidth(serverInfo.address) + 10, 18) {

            Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);
            @Override
            public boolean handleClick(double x, double y, int button) {
                if(isHovering(x, y)) {
                    Falsify.mc.keyboard.setClipboard(serverInfo.address);
                }
                return false;
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if(isHovering(mouseX, mouseY)) fade.rise(); else fade.lower();
                matrices.push();
                matrices.translate(this.x, this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), matrices, 0, 0, (float) width, (float) height, 3, new int[] {5, 5, 5, 5});
                matrices.translate(mouseX-this.x, mouseY-this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), matrices, 0, -15, (float) 50, (float) 0, 3, new int[] {5, 5, 5, 5});
                if(fade.getProgress() != 0) drawTextWithShadow(matrices, Falsify.mc.textRenderer, "Copy?", 10, -12, fade.color(new Color(255, 255, 255, 0), new Color(255, 255, 255, 254)).getRGB());
                fade.tick();
                matrices.pop();
            }
        };

        this.checkPlayers = new Clickable((width - 10 - Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString())-5), 4, Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString()) + 10, 18) {

            Animation fade = new Animation(100, Animation.Type.EASE_IN_OUT);
            @Override
            public boolean handleClick(double x, double y, int button) {
                if(isHovering(x, y)) {
                    Falsify.mc.keyboard.setClipboard(serverInfo.address);
                }
                return false;
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if(isHovering(mouseX, mouseY)) fade.rise(); else fade.lower();
                matrices.push();
                matrices.translate(this.x, this.y, 0);
                drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), matrices, 0, 0, (float) width, (float) height, 3, new int[] {5, 5, 5, 5});
                if(fade.getProgress() != 0){
                    int maxLen = 0;
                    for(GameProfile profile : serverInfo.players.sample()) {
                        int len = Falsify.mc.textRenderer.getWidth(profile.getName());
                        if(len > maxLen) maxLen = len;
                    }
                    matrices.translate(mouseX-this.x, mouseY-this.y, 0);
                    drawSmoothRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, 100)), matrices, 0, 0, (float) maxLen+20, (float) 18*serverInfo.players.sample().size(), 3, new int[] {5, 5, 5, 5});
                    for(int i = 0; i < serverInfo.players.sample().size(); i++) {
                        drawTextWithShadow(matrices, Falsify.mc.textRenderer, serverInfo.players.sample().get(i).getName(), 10, 3 + 18*i, fade.color(new Color(255, 255, 255, 0), new Color(255, 255, 255, 254)).getRGB());
                    }
                }
                fade.tick();
                matrices.pop();
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(isHovering(mouseX, mouseY) || isActive) fade.rise(); else fade.lower();

        mouseX-=this.x;
        mouseY-=this.y;
        matrices.push();
        matrices.translate(this.x, this.y, 0);
        drawBorder(matrices, 0, 0, (int) width, (int) height, Color.WHITE.getRGB());
        drawRect(fade.color(new Color(200, 200, 200, 0), new Color(200, 200, 200, (isActive) ? 150 : 100)), matrices, 0, 0, (int) width, (int) height);
        checkPlayers.render(matrices,mouseX, mouseY, delta);
        copyAddress.render(matrices, mouseX, mouseY, delta);
        drawTextWithShadow(matrices, Falsify.mc.textRenderer, serverInfo.address, 10, 10, Color.WHITE.getRGB());
        drawTextWithShadow(matrices, Falsify.mc.textRenderer, "Online Mode: " + serverInfo.online, 10, 10 + 18, Color.WHITE.getRGB());
        drawTextWithShadow(matrices, Falsify.mc.textRenderer, "Players: " + serverInfo.playerCountLabel.getString(), (int) (width - 10 - Falsify.mc.textRenderer.getWidth("Players: " + serverInfo.playerCountLabel.getString())), 10, Color.WHITE.getRGB());
        drawTextWithShadow(matrices, Falsify.mc.textRenderer, "Version: " + serverInfo.version.getString(), (int) (width - 10 - Falsify.mc.textRenderer.getWidth("Version: " + serverInfo.version.getString())), 10 + 18, Color.WHITE.getRGB());

        matrices.pop();
        fade.tick();
    }
}
