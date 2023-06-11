package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.misc.BungeeHack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BungeeGUI extends Screen {
    private TextFieldWidget fakeIPField;
    private TextFieldWidget fakeUUIDField;
    private TextFieldWidget fakeUsername;
    private ButtonWidget toggle;


    private final Screen parent;
    private BungeeHack bungeeHack;
    
    public BungeeGUI(Screen parent) {
        super(Text.of("bungee"));
        this.parent = parent;
    }

    @Override
    public void tick() {
        this.fakeIPField.tick();
        this.fakeUUIDField.tick();
    }

    @Override
    protected void init() {
        int offset = 4;
        this.bungeeHack = ModuleManager.getModule(BungeeHack.class);
        assert this.client != null;
        this.fakeIPField = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 66, 200, 20, Text.translatable("addServer.enterName"));
        this.fakeIPField.setFocused(true);
        this.fakeIPField.setEditable(true);
        this.fakeIPField.setText(bungeeHack.ip);
        this.fakeIPField.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeIPField);
        this.fakeUUIDField = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 106, 200, 20, Text.translatable("addServer.enterIp"));
        this.fakeUUIDField.setMaxLength(128);
        this.fakeIPField.setEditable(true);
        this.fakeUUIDField.setText(bungeeHack.uuid);
        this.fakeUUIDField.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeUUIDField);


        this.fakeUsername = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 146, 200, 20, Text.translatable("addServer.enterIp"));
        this.fakeUsername.setMaxLength(128);
        this.fakeIPField.setEditable(true);
        this.fakeUsername.setText(MinecraftClient.getInstance().getSession().getUsername());
        this.fakeUsername.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeUsername);

        this.toggle = this.addDrawableChild(ButtonWidget.builder(Text.of("BungeeHack: " + ((bungeeHack.toggled) ? "On" : "Off")), button -> {
            bungeeHack.toggle();
            this.toggle.setMessage(Text.of("BungeeHack: " + ((bungeeHack.toggled) ? "On" : "Off")));
        }).dimensions(this.width / 2+50+ offset, 106, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Set"), button -> ((MixinMinecraft)MinecraftClient.getInstance()).setSession(new Session(fakeUsername.getText(), "", "", null,null, Session.AccountType.MOJANG))).dimensions(width/2+50 + offset, 146, 50, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Reset"), button -> ((MixinMinecraft)MinecraftClient.getInstance()).setSession(Falsify.session)).dimensions(width/2+50+50+ offset, 146, 50, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Random IP"), button -> this.fakeIPField.setText((int)(Math.random()*255) + "." + (int)(Math.random()*255) + "." + (int)(Math.random()*255) + "."+ (int)(Math.random()*255))).dimensions(width/2+50+ offset, 66, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Go Back"), button -> this.onClose()).dimensions(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20).build());
    }

    public void onClose(String text) {
        this.bungeeHack.uuid = fakeUUIDField.getText().replace("-", "");
        this.bungeeHack.ip = fakeIPField.getText();
    }
    public void onClose() {
        this.bungeeHack.uuid = fakeUUIDField.getText().replace("-", "");
        this.bungeeHack.ip = fakeIPField.getText();
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake IP"), width/2-150, 53, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake UUID"), width/2-150, 94, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake IGN, Current: " + Falsify.mc.getSession().getUsername()), width/2-150, 134, 10526880);
        this.fakeIPField.render(matrices, mouseX, mouseY, delta);
        this.fakeUUIDField.render(matrices, mouseX, mouseY, delta);
        this.fakeUsername.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
