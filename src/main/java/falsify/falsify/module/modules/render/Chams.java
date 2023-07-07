package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventEntityRender;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Chams extends Module {

    private final ModeSetting type = new ModeSetting("Type", "All", "All", "Players", "Mobs", "Animals");
    private final ColorSetting color = new ColorSetting("Color", 255,255,255,255);

    public Chams() {
        super("Chams", "Highlights targets.", Category.RENDER, -1);
        settings.add(type);
        settings.add(color.getRed());
        settings.add(color.getGreen());
        settings.add(color.getBlue());
        settings.add(color.getAlpha());

    }



    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EventEntityRender e)) return;
        if(mc.world == null) return;
        switch (type.getMode()) {
            case "All": break;
            case "Players": if(!(e.getEntity() instanceof PlayerEntity)) {return;} break;
            case "Mobs": if(!(e.getEntity() instanceof HostileEntity)) {return;} break;
            case "Animals": if(!(e.getEntity() instanceof MobEntity)) {return;} break;
        }

        if(e.isPre()){
            Color rgba = color.getColor();
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.setShaderColor(rgba.getRed()/255f, rgba.getGreen()/255f, rgba.getBlue()/255f, rgba.getAlpha()/255f);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

            RenderSystem.disableDepthTest();
        } else if(e.isPost()) {
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1f,1f,1f,1f);
        }
    }
}
