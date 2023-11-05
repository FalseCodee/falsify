package falsify.falsify.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.utils.fonts.FontRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL32C.*;


import java.awt.*;
import java.util.List;
import java.util.SortedMap;

import static falsify.falsify.Falsify.mc;

public class RenderUtils {
    public static float windowRatio = 16f/9f;
    public static VertexConsumerProvider.Immediate vertexConsumerProvider;

    public static void init() {
        SortedMap<RenderLayer, BufferBuilder> buffers = Util.make(new Object2ObjectLinkedOpenHashMap<>(), (map) -> map.put(RenderLayer.getText(Style.DEFAULT_FONT_ID), new BufferBuilder(RenderLayer.getGui().getExpectedBufferSize())));
        vertexConsumerProvider = VertexConsumerProvider.immediate(buffers, new BufferBuilder(256));
    }

    public static void AlignFill(DrawContext context, int x1, int y1, int x2, int y2, int color, Alignment alignment) {
        switch (alignment) {
            case LEFT -> context.fill(x1, y1, x2, y2, color);
            case RIGHT -> context.fill(mc.getWindow().getScaledWidth() - x1, y1, mc.getWindow().getScaledWidth() - x2, y2, color);
            case XCENTER -> context.fill(mc.getWindow().getScaledWidth() / 2 - x1, y1, mc.getWindow().getScaledWidth() / 2 + x2, y2, color);
            case YCENTER -> context.fill(x1, mc.getWindow().getScaledHeight() / 2 - y1, x2, mc.getWindow().getScaledHeight() / 2 + y2, color);
            case CENTER -> context.fill(mc.getWindow().getScaledWidth() / 2 - x1, mc.getWindow().getScaledHeight() / 2 - y1, mc.getWindow().getScaledWidth() / 2 + x2, mc.getWindow().getScaledHeight() / 2 + y2, color);
        }
    }

    public static void AlignFillGradient(DrawContext context, int x1, int y1, int x2, int y2, int color, int color2, Alignment alignment) {
        switch (alignment) {
            case LEFT -> fillXGradient(context, x1, y1, x2, y2, color, color2);
            case RIGHT -> fillXGradient(context, mc.getWindow().getScaledWidth() - x1, y1, mc.getWindow().getScaledWidth() - x2, y2, color, color2);
            case XCENTER -> fillXGradient(context, mc.getWindow().getScaledWidth() / 2 - x1, y1, mc.getWindow().getScaledWidth() / 2 + x2, y2, color, color2);
            case YCENTER -> fillXGradient(context, x1, mc.getWindow().getScaledHeight() / 2 - y1, x2, mc.getWindow().getScaledHeight() / 2 + y2, color, color2);
            case CENTER -> fillXGradient(context, mc.getWindow().getScaledWidth() / 2 - x1, mc.getWindow().getScaledHeight() / 2 - y1, mc.getWindow().getScaledWidth() / 2 + x2, mc.getWindow().getScaledHeight() / 2 + y2, color, color2);
        }
    }

    public static void AlignText(DrawContext context, String text, float x1, float y1, Color color, Alignment alignment) {
        FontRenderer fr = Falsify.fontRenderer;
        switch (alignment) {
            case LEFT -> fr.drawString(context, text, x1, y1, color, true);
            case RIGHT -> fr.drawString(context, text, mc.getWindow().getScaledWidth() - x1 - fr.getStringWidth(text), y1, color, true);
            case XCENTER -> fr.drawString(context, text, mc.getWindow().getScaledWidth() / 2f + x1, y1, color, true);
            case YCENTER -> fr.drawString(context, text, x1, mc.getWindow().getScaledHeight() / 2f + y1, color, true);
            case CENTER -> fr.drawString(context, text, mc.getWindow().getScaledWidth() / 2f + x1, mc.getWindow().getScaledHeight() / 2f + y1, color, true);
        }
    }

    public static void AlignCenteredText(DrawContext context, String text, float x1, float y1, Color color, Alignment alignment) {
        FontRenderer fr = Falsify.fontRenderer;
        switch (alignment) {
            case LEFT -> fr.drawCenteredString(context, text, x1, y1, color, true);
            case RIGHT -> fr.drawCenteredString(context, text,mc.getWindow().getScaledWidth() - x1, y1, color, true);
            case XCENTER -> fr.drawCenteredString(context, text, mc.getWindow().getScaledWidth() / 2f + x1, y1, color, true);
            case YCENTER -> fr.drawCenteredString(context, text, x1, mc.getWindow().getScaledHeight() / 2f + y1, color, true);
            case CENTER -> fr.drawCenteredString(context, text, mc.getWindow().getScaledWidth() / 2f + x1, mc.getWindow().getScaledHeight() / 2f + y1, color, true);
        }
    }

    public static int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static float[] RGBA(int color) {
        if ((color & 0xFC000000) == 0)
            color |= 0xFF000000;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        return new float[]{red, green, blue, alpha};
    }

    public static void drawESPTracer(PlayerEntity player, Entity entity, EventRender3d eventRender3d, Color color, boolean fill) {
        Vec3d pos = MathUtils.getInterpolatedPos(entity, eventRender3d.getTickDelta());
        Box box = new Box(pos.subtract(entity.getWidth()/2, 0, entity.getWidth()/2), pos.add(entity.getWidth()/2, entity.getHeight(), entity.getWidth()/2));

        if(fill) drawBoundingBox(box, eventRender3d, color);
        else drawBoundingBoxOutline(box, eventRender3d, color);
    }

    public static void drawCenteredText(DrawContext context, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        context.drawText(textRenderer, text, centerX - textRenderer.getWidth(text) / 2, y, color, false);
    }
    public static void drawBoundingBox(Box bb, EventRender3d eventRender3d, Color color) {
        box(eventRender3d, new Vec3d[] {new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ)}, color);
    }

    public static void drawBoundingBoxOutline(Box bb, EventRender3d eventRender3d, Color color) {
        line(eventRender3d, new Vec3d[] {new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.minX, bb.minY, bb.minZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.maxX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ)}, color);
    }

    public static void line(EventRender3d eventRender3d, Vec3d from, Vec3d to, Color color) {
        MatrixStack matrices = eventRender3d.getMatrices();
        Camera camera = eventRender3d.getCamera();

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);


        Vec3d vector = to.subtract(from);

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.lineWidth(2.0f);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.LINES);
        RenderSystem.lineWidth(2.0F);
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) from.x, (float) from.y, (float) from.z).color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha() / 255F).normal(matrices.peek().getNormalMatrix(), (float) 0, (float) 0, (float) 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) to.x, (float) to.y, (float) to.z).color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha() / 255F).normal(matrices.peek().getNormalMatrix(), (float) vector.x, (float) vector.y, (float) vector.z).next();
        tessellator.draw();

        matrices.pop();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();
    }

    public static void push3DPos(MatrixStack matrices, Vec3d pos) {
        Camera camera = mc.gameRenderer.getCamera();
        matrices.push();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();
        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        matrices.translate(pos.x, pos.y, pos.z);
        matrices.multiply(camera.getRotation());
        matrices.scale(-0.025f,-0.025f,-0.025f);
    }

    public static void pop3DPos(MatrixStack matrices) {
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        matrices.pop();
    }

    public static void drawTexture(Identifier texture, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(texture, matrices, x, y, 0, (float)u, (float)v, width, height, 256, 256);
    }

    public static void drawTexture(Identifier texture, MatrixStack matrices, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(texture, matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(Identifier texture, MatrixStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        drawTexture(texture, matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(Identifier texture, MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(texture, matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    public static void drawTexture(Identifier texture, MatrixStack matrices, int x1, int x2, int y1, int y2, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(texture, matrices, x1, x2, y1, y2, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
    }

    public static void drawTexturedQuad(Identifier texture, MatrixStack matrices, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        fill(matrices, x1, y1, x2, y2, 0, color);
    }

    public static void fill(MatrixStack matrices, int x1, int x2, int y1, int y2, int z, int color) {
        RenderSystem.setShaderColor(1,1,1,1);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        int i;
        if (x1 < y1) {
            i = x1;
            x1 = y1;
            y1 = i;
        }

        if (x2 < y2) {
            i = x2;
            x2 = y2;
            y2 = i;
        }

        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)x2, (float)z).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix4f, (float)y1, (float)y2, (float)z).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix4f, (float)y1, (float)x2, (float)z).color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static void renderShader(MatrixStack matrices) {
        RenderSystem.enableBlend();
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix4f, -1, -1, 0f).next();
        bufferBuilder.vertex(matrix4f, -1, 1, 0f).next();
        bufferBuilder.vertex(matrix4f, 1, 1, 0f).next();
        bufferBuilder.vertex(matrix4f, 1, -1, 0f).next();
        BufferRenderer.draw(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static void renderShaderBegin() {
        RenderSystem.disableCull();
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
    }
    public static void renderShaderEnd() {
        RenderSystem.disableBlend();
        glDisable(GL_LINE_SMOOTH);
        RenderSystem.enableCull();

    }
    public static void line(EventRender3d eventRender3d, List<Vec3d> vec3d, Color color) {
        Vec3d[] list = new Vec3d[vec3d.size()];
        line(eventRender3d, vec3d.toArray(list), color);
    }
    public static void line(EventRender3d eventRender3d, Vec3d[] vec3d, Color color) {
        MatrixStack matrices = eventRender3d.getMatrices();
        Camera camera = eventRender3d.getCamera();

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);


        //RenderSystem.disableDepthTest();
        //RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(5.0f);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(5.0F);
        for (Vec3d to : vec3d) {
            buffer.vertex(matrices.peek().getPositionMatrix(), (float) to.x, (float) to.y, (float) to.z).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).next();//.normal(matrices.peek().getNormalMatrix(), (float) vector.x, (float) vector.y, (float) vector.z).next();
        }
        tessellator.draw();

        matrices.pop();

        //RenderSystem.enableCull();
        //RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();
    }
    public static void box(EventRender3d eventRender3d, Vec3d[] vec3d, Color color) {
        MatrixStack matrices = eventRender3d.getMatrices();
        Camera camera = eventRender3d.getCamera();

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);


        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(50.0f);

        buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(50.0F);
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) vec3d[0].x, (float) vec3d[0].y, (float) vec3d[0].z).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).next();//.normal(matrices.peek().getNormalMatrix(), (float) vec3d[0].x, (float) vec3d[0].y, (float) vec3d[0].z).next();
        for(int i = 1; i < vec3d.length; i++) {
            Vec3d from = vec3d[i-1];
            Vec3d to = vec3d[i];
            Vec3d vector = to.subtract(from);
            buffer.vertex(matrices.peek().getPositionMatrix(), (float) to.x, (float) to.y, (float) to.z).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).next();//.normal(matrices.peek().getNormalMatrix(), (float) vector.x, (float) vector.y, (float) vector.z).next();
        }
        tessellator.draw();

        matrices.pop();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();
    }

    public static void fillXGradient(DrawContext context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        float f = (float) ColorHelper.Argb.getAlpha(colorStart) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(colorStart) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(colorStart) / 255.0F;
        float i = (float) ColorHelper.Argb.getBlue(colorStart) / 255.0F;
        float j = (float) ColorHelper.Argb.getAlpha(colorEnd) / 255.0F;
        float k = (float) ColorHelper.Argb.getRed(colorEnd) / 255.0F;
        float l = (float) ColorHelper.Argb.getGreen(colorEnd) / 255.0F;
        float m = (float) ColorHelper.Argb.getBlue(colorEnd) / 255.0F;
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)startY, (float)0).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)endY, (float)0).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)endY, (float)0).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)startY, (float)0).color(k, l, m, j).next();
        context.draw();
    }
    public static void fillCornerGradient(DrawContext context, int startX, int startY, int endX, int endY, int color1, int color2, int color3, int color4) {
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        float f = (float) ColorHelper.Argb.getAlpha(color1) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(color1) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(color1) / 255.0F;
        float i = (float) ColorHelper.Argb.getBlue(color1) / 255.0F;
        float j = (float) ColorHelper.Argb.getAlpha(color2) / 255.0F;
        float k = (float) ColorHelper.Argb.getRed(color2) / 255.0F;
        float l = (float) ColorHelper.Argb.getGreen(color2) / 255.0F;
        float m = (float) ColorHelper.Argb.getBlue(color2) / 255.0F;
        float f2 = (float) ColorHelper.Argb.getAlpha(color3) / 255.0F;
        float g2 = (float) ColorHelper.Argb.getRed(color3) / 255.0F;
        float h2 = (float) ColorHelper.Argb.getGreen(color3) / 255.0F;
        float i2 = (float) ColorHelper.Argb.getBlue(color3) / 255.0F;
        float j2 = (float) ColorHelper.Argb.getAlpha(color4) / 255.0F;
        float k2 = (float) ColorHelper.Argb.getRed(color4) / 255.0F;
        float l2 = (float) ColorHelper.Argb.getGreen(color4) / 255.0F;
        float m2 = (float) ColorHelper.Argb.getBlue(color4) / 255.0F;
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)startY, (float)0).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)endY, (float)0).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)endY, (float)0).color(g2, h2, i2, f2).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)startY, (float)0).color(k2, l2, m2, j2).next();
        context.draw();
    }

    public static MatrixStack get3dMatrixStackAtCamera() {
        Camera camera = mc.gameRenderer.getCamera();
        MatrixStack matrices = new MatrixStack();
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        return matrices;
    }

}
