package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.mixin.MixinGameRenderer;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.*;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityInfo extends Module {

    private final ModeSetting type = new ModeSetting("Type", "Cursor", "Cursor", "Radius", "Aimbot");
    private final ModeSetting filter = new ModeSetting("Filter", "Players", "All", "Players", "Mobs", "Animals");
    private final RangeSetting distance = new RangeSetting("Distance", 10, 1, 100, 0.5);
    private final BooleanSetting invisibility = new BooleanSetting("Show Invisible", true);
    private final DecimalFormat format = new DecimalFormat("0.0");

    public EntityInfo() {
        super("Entity Info", "Shows information about a target.", true, Category.RENDER, -1);
        settings.add(type);
        settings.add(filter);
        settings.add(distance);
        settings.add(invisibility);
    }

    @Override
    public void onDisable() {
        clearAvatarCache();
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender e) {
            if(mc.world == null) return;
            if(type.getMode().equals("Aimbot")) {
                if(Aimbot.target == null || Aimbot.target.getEntity() == null) return;
                if(!invisibility.getValue() && Aimbot.target.getEntity().isInvisibleTo(mc.player)) return;
                drawInfoBox(e.getDrawContext(), e.getTickDelta(), Aimbot.target.getEntity());
                return;
            }
            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof LivingEntity && entity != mc.player && mc.player.squaredDistanceTo(entity) < distance.getValue()*distance.getValue()).collect(Collectors.toList());

            switch (filter.getMode()) {
                case "All": break;
                case "Players": entityList = entityList.stream().filter(PlayerEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
            }

            if(type.getMode().equals("Cursor")) {
                entityList.sort(Comparator.comparingDouble(entity -> 5 * entity.squaredDistanceTo(mc.player) + 1 * MathUtils.squaredCursorDistanceTo(entity)/5));
                if(entityList.size() == 0) return;
                if(!invisibility.getValue() && entityList.get(0).isInvisibleTo(mc.player)) return;
                drawInfoBox(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(0));
                return;
            }



            if(!invisibility.getValue()) entityList = entityList.stream().filter(entity -> !entity.isInvisibleTo(mc.player)).toList();
            entityList.sort(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(mc.player)));
            for(int i = 0; i < entityList.size(); i++) {
                e.getDrawContext().getMatrices().push();
                e.getDrawContext().getMatrices().translate(0.0, 0.0, 0.04*entityList.size()-0.04*i);
                drawInfoBox(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(i));
                e.getDrawContext().getMatrices().pop();
            }

            }
        }

    public void drawInfoBox(DrawContext context, float tickDelta, LivingEntity entity) {
        double fov = ((MixinGameRenderer.Accessor)mc.gameRenderer).getCurrentFov(mc.gameRenderer.getCamera(), tickDelta, true)/90;
        FontRenderer fr = Falsify.fontRenderer;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        Vec3d entityPos = MathUtils.getInterpolatedPos(entity, tickDelta);
        Vec2f pos = ProjectionUtils.toScreenXY(entityPos.subtract(0, 1,0));
        double dist = fov*entityPos.distanceTo(mc.gameRenderer.getCamera().getPos());
        matrices.translate(pos.x, pos.y, 0);
        matrices.scale((float) (5/dist), (float) (5/dist), 1);
        String name = entity.getName().getString();
        float xRange = (Math.max(fr.getStringWidth("WWWWWWWWWWWW"), fr.getStringWidth(name)) + 46)/2;
        RenderHelper.drawSmoothRect(new Color(40, 40, 40, 181), matrices, -xRange-1,  -26,  xRange+1, 26, 4f, new int[] {5,5,5,5});
        RenderHelper.drawSmoothRect(new Color(80, 80, 80, 182), matrices, -xRange,  -25,  xRange, 25, 3, new int[] {5,5,5,5});
//        matrices.translate(0,0,-0.03);
        LegacyIdentifier id = getHead(entity.getUuid());
        if(id == null) {
            fr.drawCenteredString(context, name, 0, -23, Color.WHITE, true);

            fr.drawCenteredString(context, "pp size: " + MathUtils.clamp(Math.abs(entity.getUuid().getMostSignificantBits() % 120 / 10f), 3, 12) + " inches", 0, -1, Color.WHITE, true);

            fr.drawCenteredString(context, entity.getHealth() + " / " + entity.getMaxHealth(), 0, 11, Color.WHITE, true);
        } else {
            Color normalColor = new Color(255, 255, 255, 255);
            Color subColor = new Color(199, 199, 199, 255);

            float width = 2.3f*id.getWidth();
            float height = 2.3f*id.getHeight();

            context.drawTexture(id.getId(), (int)-xRange+5,-id.getHeight()-5,0,0, (int) width, (int) height, (int) width, (int) height);
            matrices.push();
            matrices.translate(-xRange+44, -24, 0);
            matrices.scale(1.2f, 1.2f, 1.0f);
            fr.drawString(context, name, 0, 0, normalColor, true);
            matrices.pop();
            fr.drawString(context, "Armor: §f" + entity.getArmor() + " / 20", -xRange+44, -2, subColor, true);
            fr.drawString(context, format.format(entity.getHealth()) + " / " + format.format(entity.getMaxHealth()), -xRange+44, 9, normalColor, true);
        }
        float percentage = MathUtils.clamp(entity.getHealth()/ entity.getMaxHealth(), 0.0f, 1.0f);
        RenderUtils.fill(matrices, (int) -xRange,  20, (int) (2*xRange*percentage-xRange), 22, new Color((1-percentage), percentage, 56/255f, 219/255f).getRGB());
        matrices.pop();
    }

    public LegacyIdentifier getHead(UUID uuid) {
        if(Falsify.textureCacheManager.getIdentifier("avatar-" + uuid) == null) {
            Falsify.textureCacheManager.cacheTextureFromUrlAsync("avatar-" + uuid, "https://crafatar.com/avatars/"+uuid+"?overlay&default=MHF_Steve&size=16", false);
            return null;
        }

        return Falsify.textureCacheManager.getIdentifier("avatar-" + uuid);
    }

    public static void clearAvatarCache() {
        new ArrayList<>(Falsify.textureCacheManager.getTextures().keySet().stream().filter(s -> s.startsWith("avatar-")).toList()).forEach(s -> Falsify.textureCacheManager.destroyTexture(s));
    }
}
