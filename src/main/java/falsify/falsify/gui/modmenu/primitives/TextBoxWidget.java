package falsify.falsify.gui.modmenu.primitives;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Animation;
import falsify.falsify.gui.utils.Typable;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TextBoxWidget extends PanelWidget implements Typable {

    private String text;
    private final int maxLength = 256;
    private float textOffset = 0.0f;
    private boolean isActive;
    private boolean selecting;
    private Predicate<String> textPredicate = Objects::nonNull;

    private int firstCharacterIndex;
    private int selectionStart;
    private int selectionEnd;
    private Consumer<String> changedListener;
    private final FontRenderer textRenderer;
    private final RenderableRunnable background;
    private final Animation cursorAnimation = new Animation(250, Animation.Type.EASE_IN_OUT);

    public TextBoxWidget(Panel panel, double x, double y, double width, double height, RenderableRunnable background) {
        super(panel, x, y, width, height);
        textRenderer = Falsify.fontRenderer;
        this.background = background;
        text = "";
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        if(isHovering(x, y)) {
            isActive = !isActive;
            return true;
        }
        isActive = false;
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        pushStackToPosition(context.getMatrices());

        if(cursorAnimation.getState() == Animation.State.ACTIVE || cursorAnimation.getState() == Animation.State.LOWERING) cursorAnimation.lower();
        if(cursorAnimation.getState() == Animation.State.INACTIVE || cursorAnimation.getState() == Animation.State.RISING) cursorAnimation.rise();
        cursorAnimation.tick();
        cursorAnimation.setDuration(750);

        background.run(this, context, mouseX, mouseY, delta);
        panel.getScissorStack().push(x, y, x + width, y + height);
        textRenderer.drawString(context, text, -textOffset, 0, panel.getTheme().primaryTextColor(), true);
        if(isActive && text != null) {
            float textHeight = textRenderer.getStringHeight("I");
            float cursorPos = textRenderer.getStringWidth(text.substring(0, getCursor()))-textOffset;
            context.fill((int) cursorPos, 0, (int) (cursorPos + 1), (int) textHeight, new Color(1.0f, 1.0f, 1.0f, (float) cursorAnimation.run()).getRGB());

            int min = Math.min(selectionStart, selectionEnd);
            int max = Math.max(selectionStart, selectionEnd);
            float beginPos = textRenderer.getStringWidth(text.substring(0, min))-textOffset;
            float endPos = beginPos + textRenderer.getStringWidth(text.substring(min, max));

            context.fill((int) beginPos, 0, (int) endPos, (int) textHeight, new Color(136, 251, 255, 140).getRGB());
        }
        panel.getScissorStack().pop();
        context.getMatrices().pop();
    }

    public boolean charTyped(char chr, int modifiers) {
        if(!isActive) return false;
        if (StringHelper.isValidChar(chr)) {
            this.write(Character.toString(chr));
            return true;
        }
        return false;
    }

        @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive) {
            return false;
        }
        this.selecting = Screen.hasShiftDown();
        if (Screen.isSelectAll(keyCode)) {
            this.setCursorToEnd();
            this.setSelectionEnd(0);
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            this.write(MinecraftClient.getInstance().keyboard.getClipboard());
            return true;
        }
        if (Screen.isCut(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            this.write("");
            return true;
        }
            switch (keyCode) {
                case 263 -> {
                    if (Screen.hasControlDown()) {
                        this.setCursor(this.getWordSkipPosition(-1));
                    } else {
                        this.moveCursor(-1);
                    }
                    return true;
                }
                case 262 -> {
                    if (Screen.hasControlDown()) {
                        this.setCursor(this.getWordSkipPosition(1));
                    } else {
                        this.moveCursor(1);
                    }
                    return true;
                }
                case 259 -> {
                    this.selecting = false;
                    this.erase(-1);
                    this.selecting = Screen.hasShiftDown();

                    return true;
                }
                case 261 -> {
                    this.selecting = false;
                    this.erase(1);
                    this.selecting = Screen.hasShiftDown();
                    return true;
                }
                case 268 -> {
                    this.setCursorToStart();
                    return true;
                }
                case 269 -> {
                    this.setCursorToEnd();
                    return true;
                }
            }
        return false;
    }

    public void setText(String text) {
        if (!this.textPredicate.test(text)) {
            return;
        }
        this.text = text.length() > this.maxLength ? text.substring(0, this.maxLength) : text;
        this.setCursorToEnd();
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(text);
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        return this.text.substring(i, j);
    }

    public void setTextPredicate(Predicate<String> textPredicate) {
        this.textPredicate = textPredicate;
    }

    public void write(String text) {
        String string2;
        String string;
        int l;
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        int k = this.maxLength - this.text.length() - (i - j);
        if (k < (l = (string = StringHelper.stripInvalidChars(text)).length())) {
            string = string.substring(0, k);
            l = k;
        }
        if (!this.textPredicate.test(string2 = new StringBuilder(this.text).replace(i, j, string).toString())) {
            return;
        }
        this.text = string2;
        this.setSelectionStart(i + l);
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(this.text);
    }

    private void onChanged(String newText) {
        if (this.changedListener != null) {
            this.changedListener.accept(newText);
        }
    }

    private void erase(int offset) {
        if (Screen.hasControlDown()) {
            this.eraseWords(offset);
        } else {
            this.eraseCharacters(offset);
        }
    }

    public void setChangedListener(Consumer<String> changedListener) {
        this.changedListener = changedListener;
    }

    public void eraseWords(int wordOffset) {
        if (this.text.isEmpty()) {
            return;
        }
        if (this.selectionEnd != this.selectionStart) {
            this.write("");
            return;
        }
        this.eraseCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
    }

    public void eraseCharacters(int characterOffset) {
        int k;
        if (this.text.isEmpty()) {
            return;
        }
        if (this.selectionEnd != this.selectionStart) {
            this.write("");
            return;
        }
        int i = this.getCursorPosWithOffset(characterOffset);
        int j = Math.min(i, this.selectionStart);
        if (j == (k = Math.max(i, this.selectionStart))) {
            return;
        }
        String string = new StringBuilder(this.text).delete(j, k).toString();
        if (!this.textPredicate.test(string)) {
            return;
        }
        this.text = string;
        this.setCursor(j);
    }

    public int getWordSkipPosition(int wordOffset) {
        return this.getWordSkipPosition(wordOffset, this.getCursor());
    }

    private int getWordSkipPosition(int wordOffset, int cursorPosition) {
        return this.getWordSkipPosition(wordOffset, cursorPosition, true);
    }

    private int getWordSkipPosition(int wordOffset, int cursorPosition, boolean skipOverSpaces) {
        int i = cursorPosition;
        boolean bl = wordOffset < 0;
        int j = Math.abs(wordOffset);
        for (int k = 0; k < j; ++k) {
            if (bl) {
                while (skipOverSpaces && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }
                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
                continue;
            }
            int l = this.text.length();
            if ((i = this.text.indexOf(32, i)) == -1) {
                i = l;
                continue;
            }
            while (skipOverSpaces && i < l && this.text.charAt(i) == ' ') {
                ++i;
            }
        }
        return i;
    }

    public int getCursor() {
        return this.selectionStart;
    }

    public void moveCursor(int offset) {
        this.setCursor(this.getCursorPosWithOffset(offset));
    }

    private int getCursorPosWithOffset(int offset) {
        return Util.moveCursor(this.text, this.selectionStart, offset);
    }

    public void setCursor(int cursor) {
        this.setSelectionStart(cursor);
        if (!this.selecting) {
            this.setSelectionEnd(this.selectionStart);
        }
        this.onChanged(this.text);
    }

    public void setSelectionStart(int cursor) {
        int tmp = MathHelper.clamp(cursor, 0, this.text.length());
        boolean rising = tmp > this.selectionStart;
        this.selectionStart = tmp;
        if(rising)
            this.textOffset = (float) Math.max(textOffset, textRenderer.getStringWidth(text.substring(0, cursor)) - width);
        else
            this.textOffset = Math.min(textOffset, textRenderer.getStringWidth(text.substring(0, Math.max(0,cursor - 4))));

        cursorAnimation.setProgress(1.0);
    }

    public void setCursorToStart() {
        this.setCursor(0);
    }

    public void setCursorToEnd() {
        this.setCursor(this.text.length());
    }

    protected boolean drawsBackground() {
        return false;
    }

    public double getInnerWidth() {
        return this.drawsBackground() ? this.width - 8 : this.width;
    }


    public void setSelectionEnd(int index) {
        int i = this.text.length();
        this.selectionEnd = MathHelper.clamp(index, 0, i);
        if (this.textRenderer != null) {
            if (this.firstCharacterIndex > i) {
                this.firstCharacterIndex = i;
            }
            float j = (float) this.getInnerWidth();
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), j);
            int k = string.length() + this.firstCharacterIndex;
            if (this.selectionEnd == this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.textRenderer.trimToWidthBackwards(this.text, j).length();
            }
            if (this.selectionEnd > k) {
                this.firstCharacterIndex += this.selectionEnd - k;
            } else if (this.selectionEnd <= this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.firstCharacterIndex - this.selectionEnd;
            }
            this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, i);
        }
    }
}
