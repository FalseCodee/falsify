package falsify.falsify.gui;

import falsify.falsify.module.modules.chat.TPAnnoy;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TpAnnoyGUI extends Screen {

    private TextFieldWidget tpAnnoyField;

    private final Screen parent;

    public TpAnnoyGUI(Screen parent) {
        super(Text.of("Tp Annoy"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert this.client != null;

        this.tpAnnoyField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, Text.translatable("addServer.enterIp"));
        this.tpAnnoyField.setFocused(true);
        this.tpAnnoyField.setEditable(true);
        this.tpAnnoyField.setMaxLength(128);
        this.tpAnnoyField.setText(client.getSession().getUsername());
        this.tpAnnoyField.setChangedListener(this::onClose);
        this.addSelectableChild(this.tpAnnoyField);
        this.addDrawableChild(this.tpAnnoyField);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Go Back"), button -> this.onClose()).dimensions(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20).build());
    }

    public void onClose(String text) {
        TPAnnoy.playerToAnnoy = tpAnnoyField.getText();
    }
    public void onClose() {
        TPAnnoy.playerToAnnoy = tpAnnoyField.getText();
        TPAnnoy.run = true;
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 17, 16777215);
        context.drawTextWithShadow(this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.tpAnnoyField.render(context, mouseX, mouseY, delta);
    }

}
