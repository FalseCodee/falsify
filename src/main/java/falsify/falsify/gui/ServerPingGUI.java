package falsify.falsify.gui;

import falsify.falsify.utils.NetworkUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;

public class ServerPingGUI extends Screen {

    private TextFieldWidget serverAddressField;
    private ServerInfo serverInfo;

    public ServerPingGUI() {
        super(Text.of("Ping Server"));
    }

    @Override
    public void tick() {
        this.serverAddressField.tick();
    }

    @Override
    protected void init() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);

        this.serverAddressField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, Text.translatable("addServer.enterIp"));
        this.serverAddressField.setTextFieldFocused(true);
        this.serverAddressField.setEditable(true);
        this.serverAddressField.setMaxLength(128);
        this.serverAddressField.setText("mc.hypixel.net");
        this.addSelectableChild(this.serverAddressField);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, Text.of("Ping Server"), (buttonWidget) -> {
            this.pingServer();
        }));
    }

    public void pingServer() {
        NetworkUtils.ping(serverAddressField.getText(), serverInfo1 -> serverInfo = serverInfo1);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.serverAddressField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
        if(serverInfo == null) return;
        int count = 0;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Address: " + serverInfo.address), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
        count++;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Ping: " + serverInfo.ping), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
        count++;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Label: " + serverInfo.label.getString()), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
        count++;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Online: " + serverInfo.online), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
        count++;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Player Count Label: " + serverInfo.playerCountLabel.getString()), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
        count++;
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Version: " + serverInfo.version.getString()), width/4-100, this.height / 4 + 120 - 20 + (this.textRenderer.fontHeight + 1) * (count-8), Color.WHITE.getRGB());
    }

}
