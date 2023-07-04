package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventRender3d;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static falsify.falsify.Falsify.mc;

public class EntityInfo extends Module {

    private final ModeSetting type = new ModeSetting("Mode", "Cursor", "All", "Players", "Mobs", "Animals", "Cursor", "Aimbot");
    private final RangeSetting distance = new RangeSetting("Distance", 10, 1, 100, 0.5);
    private final BooleanSetting invisibility = new BooleanSetting("Show Invisible", true);

    public EntityInfo() {
        super("Entity Info", Category.RENDER, GLFW.GLFW_KEY_DOWN);
        settings.add(type);
        settings.add(distance);
        settings.add(invisibility);
    }

    @Override
    public void onEnable() {
        mc.getResourceManager().getAllNamespaces().forEach(System.out::println);
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
            if(type.getMode().equals("Cursor")) {
                entityList.sort(Comparator.comparingDouble(entity -> 5 * entity.squaredDistanceTo(mc.player) + 1 * MathUtils.squaredCursorDistanceTo(entity)/5));
                if(entityList.size() == 0) return;
                if(!invisibility.getValue() && entityList.get(0).isInvisibleTo(mc.player)) return;
                drawInfoBox(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(0));
                drawFace(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(0));
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
        float scale = MathUtils.clamp((float) ( (1/mc.player.squaredDistanceTo(entity))*distance.getValue()*distance.getValue()/2f), 0.25f, 1f);
        if(scale <= 0.25f) return;
        float[] pos = MathUtils.toScreenXY(MathUtils.getInterpolatedPos(entity, tickDelta).subtract(0, mc.player.getEyeHeight(mc.player.getPose())+1, 0));
        float lowerCorrection = MathUtils.clamp(mc.getWindow().getScaledHeight()/4f+pos[1]-mc.getWindow().getScaledHeight(), 0, 100);

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1);
        matrices.translate(pos[0]/scale, (pos[1]-lowerCorrection)/scale, 0);

        context.fill( -75,  -25,  75, 25, new Color(56, 56, 56, 194).getRGB());
        RenderUtils.drawCenteredText(context, mc.textRenderer, entity.getDisplayName().getString(), 0, -23, Color.WHITE.getRGB());
        RenderUtils.drawCenteredText(context, mc.textRenderer, "Can Take Damage: " + (entity.canTakeDamage() ? "Yes" : "No"), 0, -13, Color.WHITE.getRGB());

        RenderUtils.drawCenteredText(context, mc.textRenderer, entity.getHealth() + " / " + entity.getMaxHealth(), 0, 11, Color.WHITE.getRGB());
        float percentage = entity.getHealth()/ entity.getMaxHealth();
        context.fill( -75,  20, (int) (150*percentage-75), 22, new Color((1-percentage), percentage, 56/255f, 219/255f).getRGB());
//        RenderUtils.pizzaHut = new LegacyIdentifier("textures/red-apple.png");

        matrices.pop();
    }

    public void drawFace(DrawContext context, float tickDelta, LivingEntity entity) {
        float scale =3*1/mc.player.distanceTo(entity);
        float[] pos = MathUtils.toScreenXY(MathUtils.getInterpolatedPos(entity, tickDelta).subtract(0, mc.player.getEyeHeight(mc.player.getPose())-entity.getEyeHeight(entity.getPose()), 0));

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1);
        LegacyIdentifier identifier = Falsify.textureCacheManager.getIdentifier("pizza-hut");
        matrices.translate((pos[0])/scale-identifier.getWidth()/2f, (pos[1])/scale-identifier.getWidth()/2f, 0);
        context.drawTexture(identifier,0,0,0,0,identifier.getWidth(), identifier.getHeight(), identifier.getWidth(), identifier.getHeight());
        matrices.pop();
    }
}
