package falsify.falsify.gui;

import falsify.falsify.Falsify;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.BungeeHack;
import falsify.falsify.module.modules.chat.TPAnnoy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TpAnnoyGUI extends Screen {

    private TextFieldWidget TpAnnoyField;


    private final Screen parent;

    public TpAnnoyGUI(Screen parent) {
        super(Text.of("Tp Annoy"));
        this.parent = parent;
    }

    @Override
    public void tick() {
        this.TpAnnoyField.tick();
    }

    @Override
    protected void init() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);

        this.TpAnnoyField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, new TranslatableText("addServer.enterIp"));
        this.TpAnnoyField.setTextFieldFocused(true);
        this.TpAnnoyField.setEditable(true);
        this.TpAnnoyField.setMaxLength(128);
        this.TpAnnoyField.setText(client.getSession().getUsername());
        this.TpAnnoyField.setChangedListener(this::onClose);
        this.addSelectableChild(this.TpAnnoyField);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, Text.of("Go Back"), (buttonWidget) -> {
            this.onClose();
        }));
    }

    public void onClose(String text) {
        TPAnnoy.playerToAnnoy = TpAnnoyField.getText();
    }
    public void onClose() {
        TPAnnoy.playerToAnnoy = TpAnnoyField.getText();
        TPAnnoy.run = true;
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.TpAnnoyField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
