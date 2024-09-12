package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventEntityRender;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.*;
import falsify.falsify.utils.*;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private final BooleanSetting dynmapEnabled = new BooleanSetting("Dynmap", false);
    private final TextSetting urlSetting = new TextSetting("Dynmap URL", "");

    private final ArrayList<NametagEntry> dynmapEntries = new ArrayList<>();
    private final Timer dynmapUpdateTimer = new Timer();

    public Nametags() {
        super("Nametags", "Makes player nametags bigger.", true, Category.RENDER, -1);
        settings.add(filter);
        settings.add(scale);
        settings.add(background);
        settings.add(health);
        settings.add(distance);
        settings.add(ping);
        settings.add(dynmapEnabled);
        settings.add(urlSetting);
        settings.add(backgroundColor);
        settings.add(textColor);

    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventEntityRender.Label el) {
            event.setCancelled(isInFilter(el.getEntity()));
        } else if(event instanceof EventUpdate) {
            if(!dynmapEnabled.getValue() || !dynmapUpdateTimer.hasTimeElapsed(10000, true))  return;

            FalseRunnable.of(() -> {
                try {
                    JsonObject json = JsonHelper.fromUrl(urlSetting.getValue());
                    JsonArray players = json.getAsJsonArray("players");
                    ArrayList<NametagEntry> toAdd = new ArrayList<>();
                    String dimension = players.asList().stream()
                            .filter(jsonElement -> jsonElement.getAsJsonObject().get("uuid").getAsString().equals(mc.player.getUuid().toString().replace("-", "")))
                            .map(jsonElement -> jsonElement.getAsJsonObject().get("world").getAsString())
                            .findFirst().orElse("minecraft_overworld");

                    for(JsonElement ele : players.asList().stream()
                            .filter(jsonElement -> jsonElement.getAsJsonObject().get("world").getAsString().equals(dimension)).toList()) {
                        JsonObject player = ele.getAsJsonObject();
                        String name = player.get("name").getAsString();
                        UUID uuid = UUID.fromString(
                                ChatModuleUtils.addHyphensToUuid(player.get("uuid").getAsString()));
                        if(uuid.equals(mc.player.getUuid())) continue;

                        int x = player.get("x").getAsInt();
                        int y = mc.player.getBlockY();
                        int z = player.get("z").getAsInt();

                        int health = player.get("health").getAsInt();


                        toAdd.add(new NametagEntry(name, uuid, new Vec3d(x, y, z), health));
                    }

                    mc.submitAndJoin(() -> {
                        dynmapEntries.clear();
                        dynmapEntries.addAll(toAdd);
                    });
                } catch (RuntimeException ignored) {}
            }).runTaskAsync();
        }

        if(!(event instanceof EventRender eventRender)) return;

        List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof LivingEntity && entity != mc.player).collect(Collectors.toList());

        switch (filter.getMode()) {
            case "All": break;
            case "Players": entityList = entityList.stream().filter(entity -> entity instanceof PlayerEntity player && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(player.getUuid()) != null).collect(Collectors.toList()); break;
            case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
            case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
        }

        List<Entity> finalEntityList = entityList;
        dynmapEntries.stream().filter(nametagEntry -> finalEntityList.stream().noneMatch(entity -> entity.getUuid().equals(nametagEntry.uuid()))).forEach(nametagEntry ->
                renderLabel(nametagEntry.name(), nametagEntry.uuid(), nametagEntry.pos(), nametagEntry.health(), 20, eventRender.getDrawContext(), eventRender.getTickDelta(), background.getValue(), health.getValue(), distance.getValue(), ping.getValue()));

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
        Vec3d entityPos = MathUtils.getInterpolatedPos(entity, tickDelta);
        entityPos = entityPos.add(0, entity.getEyeHeight(entity.getPose())+0.75f,0);
        renderLabel(entity.getDisplayName().getString(), entity.getUuid(), entityPos, (int)entity.getHealth(), (int)entity.getMaxHealth(), context, tickDelta, drawBackground, drawHealth, drawDistance, drawPing);
    }

    private void renderLabel(String name, UUID uuid, Vec3d location, int health, int maxHealth, DrawContext context, float tickDelta, boolean drawBackground, boolean drawHealth, boolean drawDistance, boolean drawPing) {
        FontRenderer fr = Falsify.fontRenderer;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        Vec2f pos = ProjectionUtils.toScreenXY(location);
        matrices.translate(pos.x, pos.y, 0);
        matrices.scale(scale.getValue().floatValue(), scale.getValue().floatValue(), 1);

        String displayName = " " + name + " ";
        String underString = "";
        if(drawHealth) {
            underString += " §aHP: §r" + health+ "/" + maxHealth + " ";
        }

        if(drawDistance) {
            underString += " §cDist: §r" + (int) mc.player.getPos().distanceTo(location) + "m ";
        }

        if(drawPing) {
            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
            PlayerListEntry playerListEntry = null;

            if(networkHandler != null) playerListEntry = networkHandler.getPlayerListEntry(uuid);
            if(playerListEntry != null) underString += " §9Ping: §r" + playerListEntry.getLatency() + "ms ";
        }

        boolean drawUnderString = !underString.isEmpty();
        float underStringScale = 0.75f;

        if(drawUnderString) {
            matrices.translate(0, 9/underStringScale, 0);
        }

        if(drawBackground) {
            float halfWidth = Math.max(fr.getStringWidth(displayName) + 12, fr.getStringWidth(underString)*underStringScale) / 2f;RenderHelper.drawSmoothRect(backgroundColor.getValue(), matrices, -halfWidth-2, -13 - (drawUnderString ? 9*underStringScale : 0), halfWidth+2, 3, 5, new int[]{5,5,5,5});

            RenderHelper.drawSmoothRect(backgroundColor.getValue(), matrices, -halfWidth-2, -13 - (drawUnderString ? 9*underStringScale : 0), halfWidth+2, 3, 3, new int[]{5,5,5,5});
        }

        fr.drawCenteredString(context, displayName, 6, -12 - (drawUnderString ? 9*underStringScale : 0), textColor.getValue(), true);



            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
            PlayerListEntry playerListEntry = null;

            if(networkHandler != null) playerListEntry = networkHandler.getPlayerListEntry(uuid);
            if(playerListEntry != null) {
                int x = (int) (-fr.getStringWidth(displayName)/2f-12)+6;
                int y = (int) (-12 - (drawUnderString ? 9*underStringScale : 0));

                PlayerSkinDrawer.draw(context, playerListEntry.getSkinTextures().texture(), x, y, 12, true, false);
            }

        if(drawUnderString) {
            matrices.scale(underStringScale, underStringScale, 1);
            fr.drawCenteredString(context, underString, 0, -9, textColor.getValue(), true);
        }
        matrices.pop();
    }

    private record NametagEntry(String name, UUID uuid, Vec3d pos, int health) { }
}
