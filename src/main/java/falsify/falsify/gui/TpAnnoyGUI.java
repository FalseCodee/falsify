package falsify.falsify.gui;

import falsify.falsify.module.modules.chat.TPAnnoy;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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

        this.TpAnnoyField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, Text.translatable("addServer.enterIp"));
        this.TpAnnoyField.setFocused(true);
        this.TpAnnoyField.setEditable(true);
        this.TpAnnoyField.setMaxLength(128);
        this.TpAnnoyField.setText(client.getSession().getUsername());
        this.TpAnnoyField.setChangedListener(this::onClose);
        this.addSelectableChild(this.TpAnnoyField);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Go Back"), button -> this.onClose()).dimensions(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20).build());
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

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 17, 16777215);
        context.drawTextWithShadow(this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.TpAnnoyField.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

}
