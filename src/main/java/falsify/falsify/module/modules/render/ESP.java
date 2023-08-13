package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ESP extends Module {

    private final ModeSetting type = new ModeSetting("Type", "All", "All", "Players", "Mobs", "Animals");
    private final ModeSetting renderType = new ModeSetting("Render Type", "Outline", "Outline", "Box", "Glow");

    private final ColorSetting colorSetting = new ColorSetting("Color", 255,255,255,255);

    public ESP() {
        super("ESP", "Draws a box around entities.", true, Category.RENDER, -1);
        settings.add(type);
        settings.add(renderType);
        settings.add(colorSetting);
    }



    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender3d eventRender3d) {
            if(mc.world == null) return;
            if(renderType.getMode().equals("Glow")) return;

            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof LivingEntity && entity != mc.player && isValid(entity)).toList();

            for(Entity e : entityList) {
                RenderUtils.drawESPTracer(mc.player, e, eventRender3d, colorSetting.getValue(), renderType.getMode().equals("Box"));
            }
        }
    }

    public boolean isGlow() {
        return this.renderType.getMode().equals("Glow");
    }

    public Color getColor() {
        return colorSetting.getValue();
    }

    public boolean isValid(Entity entity) {
        if(!(entity instanceof LivingEntity) || entity == mc.player) return false;
        switch (type.getMode()) {
            case "All" -> {
                return true;
            }
            case "Players" -> {
                return entity instanceof PlayerEntity;
            }
            case "Mobs" -> {
                return entity instanceof HostileEntity;
            }
            case "Animals" -> {
                return entity instanceof AnimalEntity;
            }default -> {
                return false;
            }
        }
    }
}
