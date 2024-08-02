package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.gui.ping.PingResponseEntry;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.NetworkUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ServerPingGUI extends Screen {

    private TextFieldWidget serverAddressField;

    private final ArrayList<PingResponseEntry> responses;
    public ServerPingGUI() {
        super(Text.of(""));
        responses = new ArrayList<>();
    }

    int queuedMoves = 0;
    int completedMoves = 0;


    @Override
    protected void init() {
        assert this.client != null;

        this.serverAddressField = new TextFieldWidget(this.textRenderer, 10, 10, 200, 20, Text.translatable("addServer.enterIp"));
        this.serverAddressField.setFocused(true);
        this.serverAddressField.setEditable(true);
        this.serverAddressField.setMaxLength(128);
        this.serverAddressField.setText("mc.hypixel.net");
        this.addSelectableChild(this.serverAddressField);
        this.addDrawableChild(this.serverAddressField);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Ping Server"), button -> this.pingServer(serverAddressField.getText())).dimensions( 200 + 10 + 10, 10, 70, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Port Scan"), button -> this.portScan(25400, 25600, 10)).dimensions( 200 + 10 + 10 + 75, 10, 70, 20).build());
    }

    public void pingServer(String address) {
        new FalseRunnable() {
            @Override
            public void run() {
                queuedMoves++;
                Falsify.logger.info(Runtime.getRuntime().availableProcessors()+"");
                NetworkUtils.ping(address, serverInfo1 -> {if(!serverInfo1.label.getString().equals("Cannot Connect")) {addPing(serverInfo1);completedMoves++;}});
            }
        }.runTaskAsync();
    }

    public void portScan(int min, int max, int threads) {
        String address = serverAddressField.getText();
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

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 17, 16777215);

        context.enableScissor(0, 68, width, height);
        for(PingResponseEntry responseEntry : new ArrayList<>(responses)) {
            responseEntry.render(context, mouseX, mouseY, delta);
        }
        context.disableScissor();

        context.drawTextWithShadow(this.textRenderer, "Pings: " + completedMoves + "/" + queuedMoves, 200 + 10 + 10 + 75 + 75, 10, 16777215);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        amountY *= 10;
        double top = responses.getFirst().getY() + amountY;
        double bottom = responses.getLast().getY() + responses.getLast().getHeight() + amountY;
        if(bottom > height-20 && top < 70+10) {
            for (PingResponseEntry settingItem : responses) {
                settingItem.setY(settingItem.getY() + amountY);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amountX, amountY);
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
