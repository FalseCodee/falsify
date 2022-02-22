package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {
    public static void AlignFill(int x1, int y1, int x2, int y2, int color, Alignment alignment){
        MatrixStack matrix = new MatrixStack();
        switch (alignment) {
            case LEFT -> DrawableHelper.fill(matrix, x1, y1, x2, y2, color);
            case RIGHT -> DrawableHelper.fill(matrix, Falsify.mc.getWindow().getScaledWidth() - x1, y1, Falsify.mc.getWindow().getScaledWidth() - x2, y2, color);
            case XCENTER -> DrawableHelper.fill(matrix, Falsify.mc.getWindow().getScaledWidth() / 2 - x1, y1, Falsify.mc.getWindow().getScaledWidth() / 2 + x2, y2, color);
            case YCENTER -> DrawableHelper.fill(matrix, x1, Falsify.mc.getWindow().getScaledHeight() / 2 - y1, x2, Falsify.mc.getWindow().getScaledHeight() / 2 + y2, color);
            case CENTER -> DrawableHelper.fill(matrix, Falsify.mc.getWindow().getScaledWidth() / 2 - x1, Falsify.mc.getWindow().getScaledHeight() / 2 - y1, Falsify.mc.getWindow().getScaledWidth() / 2 + x2, Falsify.mc.getWindow().getScaledHeight() / 2 + y2, color);
        }
    }
    public static void AlignText(String text, int x1, int y1, int color, Alignment alignment){
        TextRenderer fr = Falsify.mc.textRenderer;
        MatrixStack matrix = new MatrixStack();
        switch (alignment) {
            case LEFT -> fr.draw(matrix, text, x1, y1, color);
            case RIGHT -> fr.draw(matrix, text, Falsify.mc.getWindow().getScaledWidth() - x1 - fr.getWidth(text), y1, color);
            case XCENTER -> fr.draw(matrix, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> fr.draw(matrix, text, x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> fr.draw(matrix, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
        }
    }
    public static void AlignCenteredText(String text, int x1, int y1, int color, Alignment alignment){
        TextRenderer fr = Falsify.mc.textRenderer;
        MatrixStack matrix = new MatrixStack();
        switch (alignment) {
            case LEFT -> DrawableHelper.drawCenteredText(matrix, fr, text, x1, y1, color);
            case RIGHT -> DrawableHelper.drawCenteredText(matrix, fr, text, Falsify.mc.getWindow().getScaledWidth() - x1, y1, color);
            case XCENTER -> DrawableHelper.drawCenteredText(matrix, fr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> DrawableHelper.drawCenteredText(matrix, fr, text, x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> DrawableHelper.drawCenteredText(matrix, fr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
        }
    }

    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

}
