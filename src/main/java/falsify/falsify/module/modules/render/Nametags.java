package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventEntityRender;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ColorSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.ProjectionUtils;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Nametags extends Module {

    private final ModeSetting filter = new ModeSetting("Filter", "Players", "All", "Players", "Mobs", "Animals");
    private final RangeSetting scale = new RangeSetting("Scale", 1, 0.25, 5, 0.05);
    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final BooleanSetting health = new BooleanSetting("Health Indicator", true);
    private final BooleanSetting distance = new BooleanSetting("Distance Indicator", true);
    private final BooleanSetting ping = new BooleanSetting("Ping Indicator", true);
    private final ColorSetting backgroundColor = new ColorSetting("Background Color", new Color(0,0,0, 110));
    private final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 255, 255, 255));
    public Nametags() {
        super("Nametags", "Makes player nametags bigger.", true, Category.RENDER, -1);
        settings.add(filter);
        settings.add(scale);
        settings.add(background);
        settings.add(health);
        settings.add(distance);
        settings.add(ping);
        settings.add(backgroundColor);
        settings.add(textColor);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventEntityRender.Label el) {
            event.setCancelled(isInFilter(el.getEntity()));
        }

        if(!(event instanceof EventRender eventRender)) return;

        List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof LivingEntity && entity != mc.player).collect(Collectors.toList());

        switch (filter.getMode()) {
            case "All": break;
            case "Players": entityList = entityList.stream().filter(entity -> entity instanceof PlayerEntity player && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(player.getUuid()) != null).collect(Collectors.toList()); break;
            case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
            case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
        }

        entityList.forEach(entity ->
                renderLabel((LivingEntity) entity, eventRender.getDrawContext(), eventRender.getTickDelta(), background.getValue(), health.getValue(), distance.getValue(), ping.getValue()));

        backgroundColor.tick();
        textColor.tick();
    }

    private boolean isInFilter(Entity entity) {
        return switch (filter.getMode()) {
            case "Players" -> entity instanceof PlayerEntity;
            case "Mobs" -> entity instanceof HostileEntity;
            case "Animals" -> entity instanceof AnimalEntity;
            default -> !(entity instanceof ArmorStandEntity);
        };
    }

    private void renderLabel(LivingEntity entity, DrawContext context, float tickDelta, boolean drawBackground, boolean drawHealth, boolean drawDistance, boolean drawPing) {
        boolean player = entity instanceof PlayerEntity;

        FontRenderer fr = Falsify.fontRenderer;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        Vec3d entityPos = MathUtils.getInterpolatedPos(entity, tickDelta);
        Vec2f pos = ProjectionUtils.toScreenXY(entityPos.add(0, entity.getEyeHeight(entity.getPose())+0.75f,0));
        matrices.translate(pos.x, pos.y, 0);
        matrices.scale(scale.getValue().floatValue(), scale.getValue().floatValue(), 1);

        String name = " " + entity.getName().getString() + " ";
        String underString = "";
        if(drawHealth) {
            underString += " §aHP: §r" + (int) entity.getHealth()+ "/" + (int) entity.getMaxHealth() + " ";
        }

        if(drawDistance) {
            underString += " §cDist: §r" + (int) mc.player.distanceTo(entity) + "m ";
        }

        if(drawPing) {
            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
            PlayerListEntry playerListEntry = null;

            if(networkHandler != null) playerListEntry = networkHandler.getPlayerListEntry(entity.getUuid());
            if(playerListEntry != null) underString += " §9Ping: §r" + playerListEntry.getLatency() + "ms ";
        }

        boolean drawUnderString = underString.length() > 0;
        float underStringScale = 0.75f;

        if(drawUnderString) {
            matrices.translate(0, 9/underStringScale, 0);
        }

        if(drawBackground) {
            float halfWidth = Math.max(fr.getStringWidth(name) + (player ? 12 : 0), fr.getStringWidth(underString)*underStringScale) / 2f;RenderHelper.drawSmoothRect(backgroundColor.getValue(), matrices, -halfWidth-2, -13 - (drawUnderString ? 9*underStringScale : 0), halfWidth+2, 3, 5, new int[]{5,5,5,5});

            RenderHelper.drawSmoothRect(backgroundColor.getValue(), matrices, -halfWidth-2, -13 - (drawUnderString ? 9*underStringScale : 0), halfWidth+2, 3, 3, new int[]{5,5,5,5});
        }

        fr.drawCenteredString(context, name, 6, -12 - (drawUnderString ? 9*underStringScale : 0), textColor.getValue(), true);

        if(entity instanceof PlayerEntity playerEntity) {
            boolean bl2 = LivingEntityRenderer.shouldFlipUpsideDown(playerEntity);
            boolean bl3 = playerEntity.isPartVisible(PlayerModelPart.HAT);

            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
            PlayerListEntry playerListEntry = null;

            if(networkHandler != null) playerListEntry = networkHandler.getPlayerListEntry(entity.getUuid());
            if(playerListEntry != null) {
                int x = (int) (-fr.getStringWidth(name)/2f-12)+6;
                int y = (int) (-12 - (drawUnderString ? 9*underStringScale : 0));

                PlayerSkinDrawer.draw(context, playerListEntry.getSkinTextures().texture(), x, y, 12, bl3, bl2);
            }
        }

        if(drawUnderString) {
            matrices.scale(underStringScale, underStringScale, 1);
            fr.drawCenteredString(context, underString, 0, -9, textColor.getValue(), true);
        }
        matrices.pop();
    }
}
