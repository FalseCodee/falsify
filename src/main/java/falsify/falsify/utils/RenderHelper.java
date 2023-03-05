package falsify.falsify.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelper extends DrawableHelper {

    public static Window WINDOW = Falsify.mc.getWindow();
    public static double SCALE = 4.0;

    public static double getScaleFactor() {
        return 1/WINDOW.getScaleFactor() * SCALE;
    }

    public static void convertToScale(MatrixStack matrices) {
        convertToScale(matrices, 1.0);
    }
    public static void convertToScale(MatrixStack matrices, double scale) {
        matrices.scale((float) (getScaleFactor() * scale),(float) (getScaleFactor() * scale),1);
    }

    public static double convertToScale(double input, double scale) {
        return input * 1 / (getScaleFactor() * getScaleFactor()) * scale;
    }

    public static double convertToScale(double input) {
        return convertToScale(input, 1.0);
    }

    public static void drawLine(Color color, MatrixStack matrices, float x1, float y1, float x2, float y2) {
        RenderSystem.setShaderColor(1,1,1,1);
        if (x1 < x2) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float i = (float) color.getAlpha() / 255.0F;
        float f = (float) color.getRed()   / 255.0F;
        float g = (float) color.getGreen() / 255.0F;
        float h = (float) color.getBlue()  / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0F).color(f, g, h, i).next();

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    public static void drawRect(Color color, MatrixStack matrices, float x1, float y1, float x2, float y2) {
        RenderSystem.setShaderColor(1,1,1,1);
        if (x1 < x2) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float i = (float) color.getAlpha() / 255.0F;
        float f = (float) color.getRed()   / 255.0F;
        float g = (float) color.getGreen() / 255.0F;
        float h = (float) color.getBlue()  / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(matrix, x1, y2, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, x2, y1, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(f, g, h, i).next();

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawSmoothRect(Color color, MatrixStack matrices, float x1, float y1, float x2, float y2, float r, int[] a) {
        RenderSystem.setShaderColor(1,1,1,1);


        x1 += r;
        y1 += r;
        x2 -= r;
        y2 -= r;

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float i = (float) color.getAlpha() / 255.0F;
        float f = (float) color.getRed()   / 255.0F;
        float g = (float) color.getGreen() / 255.0F;
        float h = (float) color.getBlue()  / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        for(int q = 0; q < 4; q++) {
            if(a[q] != 0) {
                final float[] center = {
                        (q - 1) * (q - 2) * (q - 3) / -6 * x2 +
                                (q) * (q - 2) * (q - 3) / 2 * x2 +
                                (q) * (q - 1) * (q - 3) / -2 * x1 +
                                (q) * (q - 1) * (q - 2) / 6 * x1,

                        (q - 1) * (q - 2) * (q - 3) / -6 * y2 +
                                (q) * (q - 2) * (q - 3) / 2 * y1 +
                                (q) * (q - 1) * (q - 3) / -2 * y1 +
                                (q) * (q - 1) * (q - 2) / 6 * y2
                };

                for (int s = q * 90; s < (q + 1) * 90; s += 90 / a[q]) {
                    float radians = (float) Math.toRadians(s);
                    bufferBuilder.vertex(matrix, (float) Math.sin(radians) * r + center[0], (float) Math.cos(radians) * r + center[1], 0.0F).color(f, g, h, i).next();
                }
            } else {
                final float[] center = {
                        (q - 1) * (q - 2) * (q - 3) / -6 * (x2+r) +
                                (q) * (q - 2) * (q - 3) / 2 * (x2+r) +
                                (q) * (q - 1) * (q - 3) / -2 * (x1-r) +
                                (q) * (q - 1) * (q - 2) / 6 * (x1-r),

                        (q - 1) * (q - 2) * (q - 3) / -6 * (y2+r) +
                                (q) * (q - 2) * (q - 3) / 2 * (y1-r) +
                                (q) * (q - 1) * (q - 3) / -2 * (y1-r) +
                                (q) * (q - 1) * (q - 2) / 6 * (y2+r)
                };
                bufferBuilder.vertex(matrix, center[0], center[1], 0.0F).color(f, g, h, i).next();
            }
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    public static void enableScissor(int x1, int y1, int x2, int y2) {
        RenderSystem.enableScissor((int) (x1 * Falsify.mc.getWindow().getScaleFactor()), (int) ((Falsify.mc.getWindow().getScaledHeight() - y2) * Falsify.mc.getWindow().getScaleFactor()), (int) ((x2-x1) * Falsify.mc.getWindow().getScaleFactor()), (int) ((y2-y1) * Falsify.mc.getWindow().getScaleFactor()));

    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    public static Color colorLerp(Color from, Color to, double t) {
        return new Color(
                (int) MathUtils.lerp(from.getRed(), to.getRed(), t),
                (int) MathUtils.lerp(from.getGreen(), to.getGreen(), t),
                (int) MathUtils.lerp(from.getBlue(), to.getBlue(), t),
                (int) MathUtils.lerp(from.getAlpha(), to.getAlpha(), t)
        );
    }
}