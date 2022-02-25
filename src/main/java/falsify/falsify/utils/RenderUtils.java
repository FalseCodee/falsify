package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.List;

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
        public static float[] RGBA(int color) {
            if ((color & 0xFC000000) == 0)
                color |= 0xFF000000;
            float red = (color >> 16 & 0xFF) / 255.0F;
            float green = (color >> 8 & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            float alpha = (color >> 24 & 0xFF) / 255.0F;
            return new float[] { red, green, blue, alpha };
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

        public static void drawESPTracers(List<Entity> entities) {
            Vec3d start = Falsify.mc.getEntityRenderDispatcher().camera.getPos().add(Falsify.mc.player.getRotationVector());
            GL11.glLineWidth(2.0F);
            GL11.glBegin(1);
            for (Entity entity : entities) {
                Vec3d target = entity.getBoundingBox().getCenter();
                if (entity instanceof ItemEntity) {
                    GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
                } else {
                    float intensity = Falsify.mc.player.distanceTo(entity) / 20.0F;
                    GL11.glColor4f(2.0F - intensity, intensity, 0.0F, 0.5F);
                }
                GL11.glVertex3d(start.x, start.y, start.z);
                GL11.glVertex3d(target.x, target.y, target.z);
            }
            GL11.glEnd();
        }

        public static void drawESPTracer(Entity entity) {
            Vec3d start = Falsify.mc.getEntityRenderDispatcher().camera.getPos().add(Falsify.mc.player.getRotationVector());
            GL11.glLineWidth(2.0F);
            GL11.glBegin(1);
            Vec3d target = entity.getBoundingBox().getCenter();
            if (entity instanceof ItemEntity) {
                GL11.glColor4f(0.5F, 0.5F, 1.0F, 0.5F);
            } else {
                float intensity = Falsify.mc.player.distanceTo(entity) / 20.0F;
                GL11.glColor4f(2.0F - intensity, intensity, 0.0F, 0.5F);
            }
            GL11.glVertex3d(start.x, start.y, start.z);
            GL11.glVertex3d(target.x, target.y, target.z);
            GL11.glEnd();
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
    }
