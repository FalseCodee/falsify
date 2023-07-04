package falsify.falsify.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.events.EventRender3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static falsify.falsify.Falsify.mc;

public class RenderUtils {
    public static float windowRatio = 16f/9f;

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

    public static void AlignText(DrawContext context, String text, int x1, int y1, int color, Alignment alignment) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        switch (alignment) {
            case LEFT -> context.drawTextWithShadow(tr, text, x1, y1, color);
            case RIGHT -> context.drawTextWithShadow(tr, text, mc.getWindow().getScaledWidth() - x1 - tr.getWidth(text), y1, color);
            case XCENTER -> context.drawTextWithShadow(tr, text, mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> context.drawTextWithShadow(tr, text, x1, mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> context.drawTextWithShadow(tr, text, mc.getWindow().getScaledWidth() / 2 + x1, mc.getWindow().getScaledHeight() / 2 + y1, color);
        }
    }

    public static void AlignCenteredText(DrawContext context, String text, int x1, int y1, int color, Alignment alignment) {
        TextRenderer tr = mc.textRenderer;
        switch (alignment) {
            case LEFT -> context.drawCenteredTextWithShadow(tr, text, x1, y1, color);
            case RIGHT -> context.drawCenteredTextWithShadow(tr, text, mc.getWindow().getScaledWidth() - x1, y1, color);
            case XCENTER -> context.drawCenteredTextWithShadow(tr, text, mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> context.drawCenteredTextWithShadow(tr, text, x1, mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> context.drawCenteredTextWithShadow(tr, text, mc.getWindow().getScaledWidth() / 2 + x1, mc.getWindow().getScaledHeight() / 2 + y1, color);
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

    public static void drawESPTracers(PlayerEntity player, List<Entity> entityList, EventRender3d eventRender3d) {
        for (Entity entity : entityList) {
            drawESPTracer(player, entity, eventRender3d);
        }
    }

    public static void drawESPTracer(PlayerEntity player, Entity entity, EventRender3d eventRender3d) {
        float intensity = mc.player.distanceTo(entity) / 20.0F;
        Vec3d pos = MathUtils.getInterpolatedPos(entity, eventRender3d.getTickDelta());
        Box box = new Box(pos.subtract(entity.getWidth()/2, 0, entity.getWidth()/2), pos.add(entity.getWidth()/2, entity.getHeight(), entity.getWidth()/2));
        drawBoundingBox(box, eventRender3d, Color.RED);
        //RenderSystem.enableTexture();
    }

    public static void drawCenteredText(DrawContext context, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        context.drawTextWithShadow(textRenderer, text, centerX - textRenderer.getWidth(text) / 2, y, color);
    }

    public static void drawBoundingBox(Box bb, EventRender3d eventRender3d, Color color) {
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

    public RenderUtils outline(Entity entity, float partialTicks) {
        double x = entity.prevX + (entity.getX() - entity.prevX) * partialTicks;
        double y = entity.prevY + (entity.getY() - entity.prevY) * partialTicks;
        double z = entity.prevZ + (entity.getZ() - entity.prevZ) * partialTicks;
        double halfwidth = entity.getWidth() * 0.5D;
        return outline(x - halfwidth, y, z - halfwidth, x + halfwidth, y + entity.getHeight(), z + halfwidth);
    }

    public RenderUtils outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return internal_outline(minX - mc.player.getX(), minY - mc.player.getY(), minZ - mc.player.getZ(), maxX - mc.player.getX(), maxY - mc.player.getY(), maxZ - mc.player.getZ());
    }

    protected RenderUtils internal_outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, minZ);
        return this;
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
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) from.x, (float) from.y, (float) from.z).color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F).normal(matrices.peek().getNormalMatrix(), (float) 0, (float) 0, (float) 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) to.x, (float) to.y, (float) to.z).color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F).normal(matrices.peek().getNormalMatrix(), (float) vector.x, (float) vector.y, (float) vector.z).next();
        tessellator.draw();

        matrices.pop();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();
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


        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(5.0f);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(5.0F);
        buffer.vertex(matrices.peek().getPositionMatrix(), (float) vec3d[0].x, (float) vec3d[0].y, (float) vec3d[0].z).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F).next();//.normal(matrices.peek().getNormalMatrix(), (float) vec3d[0].x, (float) vec3d[0].y, (float) vec3d[0].z).next();
        for(int i = 1; i < vec3d.length; i++) {
            Vec3d from = vec3d[i-1];
            Vec3d to = vec3d[i];
            Vec3d vector = to.subtract(from);
            buffer.vertex(matrices.peek().getPositionMatrix(), (float) to.x, (float) to.y, (float) to.z).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F).next();//.normal(matrices.peek().getNormalMatrix(), (float) vector.x, (float) vector.y, (float) vector.z).next();
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

    public static void renderLabel(String text, Vec3d pos, EventRender3d eventRender3d) {
        MatrixStack matrices = eventRender3d.getMatrices();
        Camera camera = eventRender3d.getCamera();
        double d = camera.getPos().squaredDistanceTo(pos);
        if (!(d > 4096.0)) {

            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);

            matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
            matrices.translate(pos.x, pos.y, pos.z);

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = mc.textRenderer;
            float h = (float)(-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, (float)0, 553648127, false, matrix4f, (VertexConsumerProvider) buffer, TextRenderer.TextLayerType.SEE_THROUGH, j, 15);

            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            matrices.pop();
        }
    }
    public static void invertBob(MatrixStack matrices, float tickDelta) {
        if (mc.getCameraEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) mc.getCameraEntity();
            float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
            float g = -(playerEntity.horizontalSpeed + f * tickDelta);
            float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
            matrices.translate(-MathHelper.sin(g * 3.1415927F) * h * 0.5F, Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F).invert());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F).invert());
        }
    }

}
