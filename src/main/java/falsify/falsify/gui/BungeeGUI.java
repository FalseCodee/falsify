package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.BungeeHack;
import falsify.falsify.utils.ReflectionUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.lang.reflect.Field;

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
        this.bungeeHack = (BungeeHack) ModuleManager.getModule(new BungeeHack());
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);
        this.fakeIPField = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 66, 200, 20, new TranslatableText("addServer.enterName"));
        this.fakeIPField.setTextFieldFocused(true);
        this.fakeIPField.setEditable(true);
        this.fakeIPField.setText(bungeeHack.ip);
        this.fakeIPField.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeIPField);

        this.fakeUUIDField = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 106, 200, 20, new TranslatableText("addServer.enterIp"));
        this.fakeUUIDField.setMaxLength(128);
        this.fakeIPField.setEditable(true);
        this.fakeUUIDField.setText(bungeeHack.uuid);
        this.fakeUUIDField.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeUUIDField);


        this.fakeUsername = new TextFieldWidget(this.textRenderer, width/2-205+50+ offset, 146, 200, 20, new TranslatableText("addServer.enterIp"));
        this.fakeUsername.setMaxLength(128);
        this.fakeIPField.setEditable(true);
        this.fakeUsername.setText(MinecraftClient.getInstance().getSession().getUsername());
        this.fakeUsername.setChangedListener(this::onClose);
        this.addSelectableChild(this.fakeUsername);

        this.toggle = (ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2+50+ offset, 106, 100, 20, Text.of("BungeeHack: " + ((bungeeHack.toggled) ? "On" : "Off")), (buttonWidget) -> {
            bungeeHack.toggle();
            this.toggle.setMessage(Text.of("BungeeHack: " + ((bungeeHack.toggled) ? "On" : "Off")));
        }));

        this.addDrawableChild(new ButtonWidget(width/2+50 + offset, 146, 50, 20, Text.of("Set"), (buttonWidget) -> {
            ((MixinMinecraft)MinecraftClient.getInstance()).setSession(new Session(fakeUsername.getText(), "", "", null,null, Session.AccountType.MOJANG));
        }));

        this.addDrawableChild(new ButtonWidget(width/2+50+50+ offset, 146, 50, 20, Text.of("Reset"), (buttonWidget) -> {
            ((MixinMinecraft)MinecraftClient.getInstance()).setSession(Falsify.session);

        }));

        this.addDrawableChild(new ButtonWidget(width/2+50+ offset, 66, 100, 20, Text.of("Random IP"), (buttonWidget) -> {
            this.fakeIPField.setText((int)(Math.random()*255) + "." + (int)(Math.random()*255) + "." + (int)(Math.random()*255) + "."+ (int)(Math.random()*255));
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, Text.of("Go Back"), (buttonWidget) -> {
            this.onClose();
        }));
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
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake IP"), width/2-150, 53, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake UUID"), width/2-150, 94, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Fake IGN, Current: " + Falsify.mc.getSession().getUsername()), width/2-150, 134, 10526880);
        this.fakeIPField.render(matrices, mouseX, mouseY, delta);
        this.fakeUUIDField.render(matrices, mouseX, mouseY, delta);
        this.fakeUsername.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
