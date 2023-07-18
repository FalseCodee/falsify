package falsify.falsify.gui.credits;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.playerdata.PlayerData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class PlayerDataEntry extends Clickable {
    private final PlayerData playerData;
    public PlayerDataEntry(PlayerData playerData, double x, double y) {
        super(x, y, 200, 300);
        this.playerData = playerData;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        int sh = Falsify.mc.getWindow().getScaledHeight();
        matrices.translate(x,sh/8f,0);
        matrices.scale((float) (3*sh/height/4f), (float) (3*sh/height/4f), 1);
        RenderHelper.drawSmoothRect(new Color(79, 79, 79, 79), matrices, 0f,0f, (float) width, (float) height, 30, new int[] {6,6,6,6});
        Falsify.fontRenderer.drawCenteredString(matrices, playerData.getUserName(), (float) width/2, (float) height-18, Color.WHITE, true);
        LegacyIdentifier id = playerData.getBodyRender();
        matrices.translate(width/2, height/2, 0);
        matrices.scale(0.75f, 0.75f, 1);
        context.drawTexture(id,  -id.getWidth()/2, -id.getHeight()/2, 0,0, id.getWidth(), id.getHeight(), id.getWidth(), id.getHeight());
        matrices.pop();
    }
}
