package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.settings.SettingItem;
import falsify.falsify.gui.ping.PingResponseEntry;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.NetworkUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerPingGUI extends Screen {

    private TextFieldWidget serverAddressField;

    private ArrayList<PingResponseEntry> responses;
    public ServerPingGUI() {
        super(Text.of(""));
        responses = new ArrayList<>();
    }

    int queuedMoves = 0;
    int completedMoves = 0;

    @Override
    public void tick() {
        this.serverAddressField.tick();
    }

    @Override
    protected void init() {
        assert this.client != null;

        this.serverAddressField = new TextFieldWidget(this.textRenderer, 10, 10, 200, 20, Text.translatable("addServer.enterIp"));
        this.serverAddressField.setFocused(true);
        this.serverAddressField.setEditable(true);
        this.serverAddressField.setMaxLength(128);
        this.serverAddressField.setText("mc.hypixel.net");
        this.addSelectableChild(this.serverAddressField);
        this.addDrawableChild(ButtonWidget.builder(Text.of("Ping Server"), button -> this.pingServer(serverAddressField.getText().toString())).dimensions( 200 + 10 + 10, 10, 70, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Port Scan"), button -> this.portScan(25400, 25600, 10)).dimensions( 200 + 10 + 10 + 75, 10, 70, 20).build());
    }

    public void pingServer(String address) {
        new FalseRunnable() {
            @Override
            public void run() {
                queuedMoves++;
                System.out.println(Runtime.getRuntime().availableProcessors());
                NetworkUtils.ping(address, serverInfo1 -> {if(!serverInfo1.label.getString().equals("Cannot Connect")) {addPing(serverInfo1);completedMoves++;}});
            }
        }.runTaskAsync();
    }

    public void portScan(int min, int max, int threads) {
        String address = serverAddressField.getText().toString();
        if(address.contains(":")) address = address.substring(0, address.indexOf(":"));

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threads);
        int chunk = (max-min)/threads;

        for(int i = 0; i < threads; i++) {
            int finalI = i;
            String finalAddress = address;
            executor.execute(() -> {
                for(int j = min + chunk * finalI; j < min + chunk*(finalI+1); j++) {
                    queuedMoves++;
                    NetworkUtils.ping(finalAddress + ":" + j, serverInfo1 -> {if(!serverInfo1.label.getString().equals("Cannot Connect")) {addPing(serverInfo1);completedMoves++;}});
                }
            });
        }
        executor.shutdown();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        //drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.serverAddressField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
        enableScissor(0, 68, width, height);
        for(PingResponseEntry responseEntry : new ArrayList<>(responses)) {
            responseEntry.render(matrices, mouseX, mouseY, delta);
        }
        disableScissor();

        drawTextWithShadow(matrices, this.textRenderer, "Pings: " + completedMoves + "/" + queuedMoves, 200 + 10 + 10 + 75 + 75, 10, 16777215);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(PingResponseEntry responseEntry : new ArrayList<>(responses)) {
            if(responseEntry.handleClick(mouseX, mouseY, button)) {
                if(responseEntry.isActive) {
                    for(PingResponseEntry responseEntry2 : new ArrayList<>(responses)) {
                        if(responseEntry2 != responseEntry && responseEntry2.isActive) responseEntry2.isActive = false;
                    }
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        amount *= 10;
        double top = responses.get(0).getY() + amount;
        double bottom = responses.get(responses.size()-1).getY() + responses.get(responses.size()-1).getHeight() + amount;
        if(bottom > height-20 && top < 70+10) {
            for (PingResponseEntry settingItem : responses) {
                settingItem.setY(settingItem.getY() + amount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private void addPing(ServerInfo info) {
        new FalseRunnable() {
            @Override
            public void run() {
                responses.add(new PingResponseEntry(info, 10, 70 + 51*responses.size()));
            }
        }.runTaskLater(1);
    }

}
