package falsify.falsify.gui;

import falsify.falsify.module.modules.TextBrush;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextBrushGUI extends Screen {

    private TextFieldWidget argField;
    private TextFieldWidget delayField;


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

        this.argField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 - 18, 200, 20, Text.translatable("addServer.enterIp"));
        this.argField.setTextFieldFocused(true);
        this.argField.setEditable(true);
        this.argField.setMaxLength(128);
        this.argField.setText(TextBrush.theString);
        this.argField.setChangedListener(this::onChangeArg);
        this.addSelectableChild(this.argField);

        this.delayField = new TextFieldWidget(this.textRenderer, width/2-100, this.height / 4 + 120 + 18, 200, 20, Text.translatable("addServer.enterIp"));
        this.delayField.setEditable(true);
        this.delayField.setMaxLength(128);
        this.delayField.setText(String.valueOf(TextBrush.theDelay));
        this.delayField.setChangedListener(this::onChangeDelay);
        this.addSelectableChild(this.delayField);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Go Back"), button -> this.onClose()).dimensions(this.width / 2 - 100, this.height / 4 + 140 + 22, 200, 20).build());
    }

    public void onChangeArg(String text) {
        TextBrush.theString = argField.getText();
    }
    public void onChangeDelay(String text) {
        try {
            TextBrush.theDelay = Long.parseLong(text);
        }  catch (NumberFormatException ignored) {}
    }
    public void onClose() {
        TextBrush.theString = argField.getText();
        try {
            TextBrush.theDelay = Long.parseLong(delayField.getText());
        }  catch (NumberFormatException ignored) {}

        TextBrush.run = true;
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, Text.of("Enter Name of Recipient"), width/2-100, this.height / 4 + 120 - 34, 10526880);
        this.argField.render(matrices, mouseX, mouseY, delta);
        this.delayField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
