package falsify.falsify.waypoints;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Animation;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.*;
import falsify.falsify.utils.ColorUtils;
import falsify.falsify.utils.PlayerUtils;
import falsify.falsify.utils.ProjectionUtils;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class Waypoint extends Module {

    private final TextSetting title;
    private final ModeSetting dimension = new ModeSetting("Dimension", "Overworld", "Overworld", "Nether", "End");
    private final FreeNumberSetting xPos;
    private final FreeNumberSetting yPos;
    private final FreeNumberSetting zPos;
    private final BooleanSetting showRange = new BooleanSetting("Show Range", true);
    private final RangeSetting hideWhenCloser = new RangeSetting("Hide When Closer Than", 50, 0, 1000, 1);
    private final RangeSetting scale = new RangeSetting("Scale", 0.8, 0.01, 3, 0.01);
    private final ColorSetting textColor = new ColorSetting("Text Color", Color.WHITE);
    private final ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(20, 20, 20, 100));

    private final Animation fadeWaypoint = new Animation(100, Animation.Type.EASE_IN_OUT);

    public Waypoint(String title, Vec3i location) {
        super("Waypoint", "Saves a location for later.", Category.MISC, -1, true, false);
        this.title = new TextSetting("Title", title);
        this.dimension.setIndex(PlayerUtils.getDimension().getIndex());
        this.xPos = new FreeNumberSetting("X", location.getX(), 1);
        this.yPos = new FreeNumberSetting("Y", location.getY(), 1);
        this.zPos = new FreeNumberSetting("Z", location.getZ(), 1);

        settings.add(this.title);
        settings.add(this.dimension);
        settings.add(this.xPos);
        settings.add(this.yPos);
        settings.add(this.zPos);
        settings.add(this.scale);
        settings.add(this.showRange);
        settings.add(this.hideWhenCloser);
        settings.add(textColor);
        settings.add(backgroundColor);
    }

    @Override
    public void toggle(){
        this.toggled = !this.toggled;
        if(toggled){
            Falsify.waypointManager.enabledWaypoints.add(this);
            onEnable();
        } else{
            Falsify.waypointManager.enabledWaypoints.remove(this);
            onDisable();
        }
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender render) {
            if(PlayerUtils.getDimension().getIndex() != dimension.getIndex()) return;

            double squaredDistance = getLocation().getSquaredDistance(mc.player.getPos());

            if(squaredDistance < Math.pow(hideWhenCloser.getValue(), 2)){
                fadeWaypoint.lower();
            } else {
                fadeWaypoint.rise();
            }

            if(fadeWaypoint.getState() == Animation.State.INACTIVE)
                return;

            textColor.tick();
            backgroundColor.tick();
            fadeWaypoint.tick();

            Vec2f projected = ProjectionUtils.toScreenXY(new Vec3d(xPos.getValue(), yPos.getValue(), zPos.getValue()));

            MatrixStack mat = render.getDrawContext().getMatrices();
            mat.push();
            mat.translate(projected.x, projected.y, 0);
            mat.scale(scale.getValue().floatValue(), scale.getValue().floatValue(), 1);
            String text = getTitle();
            if(showRange.getValue())
                text += "\n(" + (int)Math.sqrt(squaredDistance) + "m)";
            float halfWidth = Falsify.fontRenderer.getStringWidth(text) / 2;
            float halfHeight = Falsify.fontRenderer.getStringHeight(text) / 2;

            RenderHelper.drawSmoothRect(ColorUtils.setAlpha(backgroundColor.getValue(), (float) (backgroundColor.getValue().getAlpha() * fadeWaypoint.run()/255)), mat, -halfWidth - 2, -2, halfWidth + 2, halfHeight*2 + 2, 4, new int[] {5, 5, 5, 5});
            Falsify.fontRenderer.drawCenteredString(render.getDrawContext(), text, 0, 0, ColorUtils.setAlpha(textColor.getValue(), (float) (textColor.getValue().getAlpha() * fadeWaypoint.run()/255)), true);
            mat.pop();
        }
    }

    public Waypoint(String title, int x, int y, int z) {
        this(title, new Vec3i(x, y, z));
    }

    public String getTitle() {
        return title.getValue();
    }

    public Vec3i getLocation() {
        return new Vec3i(xPos.getValue().intValue(), yPos.getValue().intValue(), zPos.getValue().intValue());
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setX(int x) {
        xPos.setValue((double) x);
    }

    public void setY(int y) {
        xPos.setValue((double) y);
    }

    public void setZ(int z) {
        xPos.setValue((double) z);
    }
}
