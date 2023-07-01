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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RenderUtils {
    public static void AlignFill(DrawContext context, int x1, int y1, int x2, int y2, int color, Alignment alignment) {
        switch (alignment) {
            case LEFT -> context.fill(x1, y1, x2, y2, color);
            case RIGHT -> context.fill(Falsify.mc.getWindow().getScaledWidth() - x1, y1, Falsify.mc.getWindow().getScaledWidth() - x2, y2, color);
            case XCENTER -> context.fill(Falsify.mc.getWindow().getScaledWidth() / 2 - x1, y1, Falsify.mc.getWindow().getScaledWidth() / 2 + x2, y2, color);
            case YCENTER -> context.fill(x1, Falsify.mc.getWindow().getScaledHeight() / 2 - y1, x2, Falsify.mc.getWindow().getScaledHeight() / 2 + y2, color);
            case CENTER -> context.fill(Falsify.mc.getWindow().getScaledWidth() / 2 - x1, Falsify.mc.getWindow().getScaledHeight() / 2 - y1, Falsify.mc.getWindow().getScaledWidth() / 2 + x2, Falsify.mc.getWindow().getScaledHeight() / 2 + y2, color);
        }
    }

    public static void AlignText(DrawContext context, String text, int x1, int y1, int color, Alignment alignment) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        switch (alignment) {
            case LEFT -> context.drawTextWithShadow(tr, text, x1, y1, color);
            case RIGHT -> context.drawTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() - x1 - tr.getWidth(text), y1, color);
            case XCENTER -> context.drawTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> context.drawTextWithShadow(tr, text, x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> context.drawTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
        }
    }

    public static void AlignCenteredText(DrawContext context, String text, int x1, int y1, int color, Alignment alignment) {
        TextRenderer tr = Falsify.mc.textRenderer;
        switch (alignment) {
            case LEFT -> context.drawCenteredTextWithShadow(tr, text, x1, y1, color);
            case RIGHT -> context.drawCenteredTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() - x1, y1, color);
            case XCENTER -> context.drawCenteredTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, y1, color);
            case YCENTER -> context.drawCenteredTextWithShadow(tr, text, x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
            case CENTER -> context.drawCenteredTextWithShadow(tr, text, Falsify.mc.getWindow().getScaledWidth() / 2 + x1, Falsify.mc.getWindow().getScaledHeight() / 2 + y1, color);
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

    public static void drawOutlinedBox(Box bb) {
        GL11.glBegin(1);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glEnd();
    }

    public static void drawSolidBox(Box bb) {
        GL11.glBegin(7);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glEnd();
    }

    public static void drawESPBoxes(List<Entity> entities, int box, float partialTicks) {
        GL11.glLineWidth(2.0F);
        for (Entity entity : entities) {
            GL11.glPushMatrix();
            Vec3d interpolated = MathUtils.getInterpolatedPos(entity, partialTicks);
            GL11.glTranslated(interpolated.x, interpolated.y, interpolated.z);
            GL11.glScaled(entity.getWidth() + 0.1D, entity.getHeight() + 0.1D, entity.getWidth() + 0.1D);
            if (entity instanceof ItemEntity) {
                GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
            } else {
                float intensity = Falsify.mc.player.distanceTo(entity) / 20.0F;
                GL11.glColor4f(2.0F - intensity, intensity, 0.0F, 0.5F);
            }
            GL11.glCallList(box);
            GL11.glPopMatrix();
        }
    }

    public static void drawESPTracers(PlayerEntity player, List<Entity> entityList, EventRender3d eventRender3d) {
        for (Entity entity : entityList) {
            drawESPTracer(player, entity, eventRender3d);
        }
    }

    public static void drawESPTracer(PlayerEntity player, Entity entity, EventRender3d eventRender3d) {
        float intensity = Falsify.mc.player.distanceTo(entity) / 20.0F;
        Vec3d pos = MathUtils.interpolateVec3d(entity.getPos(), new Vec3d(entity.prevX, entity.prevY, entity.prevZ), 0.5f);
        Box box = new Box(pos.subtract(entity.getWidth()/2, 0, entity.getWidth()/2), pos.add(entity.getWidth()/2, entity.getHeight(), entity.getWidth()/2));
        drawBoundingBox(box, eventRender3d, Color.WHITE);
        renderLabel("poop", entity.getEyePos().add(0.0, 0.5,0.0), eventRender3d);
        //RenderSystem.enableTexture();
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
        return internal_outline(minX - Falsify.mc.player.getX(), minY - Falsify.mc.player.getY(), minZ - Falsify.mc.player.getZ(), maxX - Falsify.mc.player.getX(), maxY - Falsify.mc.player.getY(), maxZ - Falsify.mc.player.getZ());
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

            matrices.multiply(camera.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = Falsify.mc.textRenderer;
            float h = (float)(-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, (float)0, 553648127, false, matrix4f, (VertexConsumerProvider) buffer, TextRenderer.TextLayerType.SEE_THROUGH, j, 15);

            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            matrices.pop();
        }
    }
    public static void invertBob(MatrixStack matrices, float tickDelta) {
        if (Falsify.mc.getCameraEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)Falsify.mc.getCameraEntity();
            float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
            float g = -(playerEntity.horizontalSpeed + f * tickDelta);
            float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
            matrices.translate(-MathHelper.sin(g * 3.1415927F) * h * 0.5F, Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F).invert());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F).invert());
        }
    }

}
