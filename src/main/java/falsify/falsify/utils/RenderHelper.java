package falsify.falsify.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;

public class RenderHelper extends DrawableHelper {
    protected static void drawRect(Color color, MatrixStack matrices, int x1, int y1, int x2, int y2) {
        RenderSystem.setShaderColor(1,1,1,1);
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int i = y1;
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
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(f, g, h, i).next();
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(f, g, h, i).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
