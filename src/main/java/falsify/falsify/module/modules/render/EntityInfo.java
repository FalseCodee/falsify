package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.*;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityInfo extends Module {

    private final ModeSetting type = new ModeSetting("Mode", "Cursor", "All", "Players", "Mobs", "Animals", "Cursor", "Aimbot");
    private final RangeSetting distance = new RangeSetting("Distance", 10, 1, 100, 0.5);
    private final BooleanSetting invisibility = new BooleanSetting("Show Invisible", true);

    public EntityInfo() {
        super("Entity Info", "Shows information about a target.", true, Category.RENDER, -1);
        settings.add(type);
        settings.add(distance);
        settings.add(invisibility);
    }

    @Override
    public void onEnable() {}

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
            if(type.getMode().equals("Cursor")) {
                entityList.sort(Comparator.comparingDouble(entity -> 5 * entity.squaredDistanceTo(mc.player) + 1 * MathUtils.squaredCursorDistanceTo(entity)/5));
                if(entityList.size() == 0) return;
                if(!invisibility.getValue() && entityList.get(0).isInvisibleTo(mc.player)) return;
                drawInfoBox(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(0));
                return;
            }

            switch (type.getMode()) {
                case "All": break;
                case "Players": entityList = entityList.stream().filter(PlayerEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
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
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        Vec3d entityPos = MathUtils.getInterpolatedPos(entity, tickDelta);
        Vec2f pos = ProjectionUtils.toScreenXY(entityPos.subtract(0, 1,0));
        double dist = entityPos.distanceTo(mc.gameRenderer.getCamera().getPos());
        matrices.translate(pos.x, pos.y, 0);
        matrices.scale((float) (5/dist), (float) (5/dist), 1);
        //RenderUtils.push3DPos(matrices, MathUtils.getInterpolatedPos(entity, tickDelta));

        RenderUtils.fill(matrices, -75,  -25,  75, 25, new Color(56, 56, 56, 194).getRGB());
        //RenderUtils.drawText(eventRender3d, pos.add(0, entity.getNameLabelHeight(), 0), entity.getDisplayName().getString(), Color.WHITE);
        context.drawCenteredTextWithShadow(mc.textRenderer, entity.getDisplayName().getString(), 0, -23, Color.WHITE.getRGB());
        if(entity instanceof PlayerEntity playerEntity) {
            context.drawCenteredTextWithShadow(mc.textRenderer, "UUID: " + playerEntity.getGameProfile().getId().toString(), 0, -13, Color.WHITE.getRGB());
        } else {
            context.drawCenteredTextWithShadow(mc.textRenderer, "Id: " + entity.getId(), 0, -13, Color.WHITE.getRGB());
        }
        context.drawCenteredTextWithShadow(mc.textRenderer, entity.getHealth() + " / " + entity.getMaxHealth(), 0, 11, Color.WHITE.getRGB());
        float percentage = MathUtils.clamp(entity.getHealth()/ entity.getMaxHealth(), 0.0f, 1.0f);
        RenderUtils.fill(matrices, -75,  20, (int) (150*percentage-75), 22, new Color((1-percentage), percentage, 56/255f, 219/255f).getRGB());
        RenderUtils.pop3DPos(matrices);
    }
}
