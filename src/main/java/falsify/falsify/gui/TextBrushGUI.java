package falsify.falsify.gui;

import falsify.falsify.module.modules.TextBrush;
import falsify.falsify.module.modules.chat.TPAnnoy;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TextBrushGUI extends Screen {

    private TextFieldWidget argField;


    private final Screen parent;

    public TextBrushGUI(Screen parent) {
        super(Text.of("Tp Annoy"));
        this.parent = parent;
    }

    @Override
    public void tick() {
        this.argField.tick();
    }

    @Override
    protected void init() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);

        this.argField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, new TranslatableText("addServer.enterIp"));
        this.argField.setTextFieldFocused(true);
        this.argField.setEditable(true);
        this.argField.setMaxLength(128);
        this.argField.setText(TextBrush.theString);
        this.argField.setChangedListener(this::onClose);
        this.addSelectableChild(this.argField);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, Text.of("Go Back"), (buttonWidget) -> {
            this.onClose();
        }));
    }

    public void onClose(String text) {
        TextBrush.theString = argField.getText();
    }
    public void onClose() {
        TextBrush.theString = argField.getText();
        TextBrush.run = true;
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.argField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
