package falsify.falsify.module.modules.render;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Breadcrumbs extends Module {

    private final RangeSetting count = new RangeSetting("Line Count", 50, 1,500, 1);
    private final RangeSetting quality = new RangeSetting("Line Quality", 5, 1,10, 0.1);
    private final ArrayList<Vec3d> crumbs = new ArrayList<>();
    private final ColorSetting colorSetting = new ColorSetting("Color", 255,255,255,255);

    public Breadcrumbs() {
        super("Breadcrumbs", "Draws a line from where you've been.", true, Category.RENDER, -1);
        settings.add(count);
        settings.add(quality);
        settings.add(colorSetting.getRed());
        settings.add(colorSetting.getGreen());
        settings.add(colorSetting.getBlue());
        settings.add(colorSetting.getAlpha());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        crumbs.clear();
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender3d eventRender3d) {
            if(mc.world == null || crumbs.size() == 0) return;
            RenderUtils.line(eventRender3d, crumbs, colorSetting.getColor());

        } else if(event instanceof EventUpdate) {
            if(mc.player == null) return;
            if(crumbs.size() == 0) crumbs.add(mc.player.getPos());
            else if(mc.player.getPos().squaredDistanceTo(crumbs.get(crumbs.size()-1)) > 0.01*(101-quality.getValue()*10)) crumbs.add(mc.player.getPos());

            while (crumbs.size() > count.getValue()) crumbs.remove(0);
        }
    }
}
