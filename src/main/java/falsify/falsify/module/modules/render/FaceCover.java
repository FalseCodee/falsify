package falsify.falsify.module.modules.render;

import com.google.common.collect.Lists;
import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.mixin.MixinGameRenderer;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.ProjectionUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.util.math.Vec2f;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FaceCover extends Module {

    private final ModeSetting type = new ModeSetting("Mode", "Cursor", "All", "Players", "Mobs", "Animals", "Cursor", "Aimbot");
    private final RangeSetting distance = new RangeSetting("Distance", 10, 1, 100, 0.5);
    private final RangeSetting faceScale = new RangeSetting("Scale", 2, 0.1, 10, 0.1);
    private final BooleanSetting invisibility = new BooleanSetting("Show Invisible", true);

    private LegacyIdentifier identifier;

    public FaceCover() {
        super("Face Cover", "Covers the face of targets with an image.", true, Category.RENDER, -1);
        settings.add(type);
        settings.add(distance);
        settings.add(faceScale);
        settings.add(invisibility);
    }

    private void setIdentifier() {
        CompletableFuture<LegacyIdentifier> futureIdentifier = Falsify.textureCacheManager.getIdentifier("pizza-hut");
        if(futureIdentifier.isDone()) {
            this.identifier = futureIdentifier.getNow(null);
        }
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender e && identifier != null) {
            if(mc.world == null) return;
            if(type.getMode().equals("Aimbot")) {
                if(Aimbot.target == null || Aimbot.target.getEntity() == null) return;
                if(!invisibility.getValue() && Aimbot.target.getEntity().isInvisibleTo(mc.player)) return;
                drawFace(e.getDrawContext(), e.getTickDelta(), Aimbot.target.getEntity());
                return;
            }
            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof LivingEntity && entity != mc.player && mc.player.squaredDistanceTo(entity) < distance.getValue()*distance.getValue()).collect(Collectors.toList());
            if(type.getMode().equals("Cursor")) {
                entityList.sort(Comparator.comparingDouble(entity -> 5 * entity.squaredDistanceTo(mc.player) + 1 * MathUtils.squaredCursorDistanceTo(entity)/5));
                if(entityList.size() == 0) return;
                if(!invisibility.getValue() && entityList.get(0).isInvisibleTo(mc.player)) return;
                drawFace(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(0));
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
                drawFace(e.getDrawContext(), e.getTickDelta(), (LivingEntity) entityList.get(i));
                e.getDrawContext().getMatrices().pop();
            }

        } else if(event instanceof EventPacketSend ep && ep.getPacket() instanceof ChatMessageC2SPacket packet && packet.chatMessage().toLowerCase().startsWith(".upload ")) {
            String urlString = packet.chatMessage().substring(".upload ".length());
            if(urlString.length() == 0) return;
            identifier = null;
            Falsify.textureCacheManager.destroyTexture("pizza-hut");
            Falsify.textureCacheManager.cacheTextureFromUrlAsync("pizza-hut", urlString, false);
            ep.setCancelled(true);
        } else if(event instanceof EventUpdate && identifier == null) {
            setIdentifier();
        }
    }

    public void drawFace(DrawContext context, float tickDelta, LivingEntity entity) {
        double fov = 1/((MixinGameRenderer.Accessor)mc.gameRenderer).getCurrentFov(mc.gameRenderer.getCamera(), tickDelta, true)*90;
        float scale = (float) (fov*faceScale.getValue().floatValue()*1/mc.gameRenderer.getCamera().getPos().distanceTo(MathUtils.interpolateEntity(entity, tickDelta)));
        Vec2f pos = ProjectionUtils.toScreenXY(MathUtils.getInterpolatedPos(entity, tickDelta).add(0, entity.getEyeHeight(entity.getPose()), 0));
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(pos.x, pos.y, 0);
        matrices.scale(scale, scale, 1);
        matrices.translate(-identifier.getWidth()/2f, -identifier.getWidth()/2f, 0);
        context.drawTexture(identifier, 0,0,0,0,identifier.getWidth(), identifier.getHeight(), identifier.getWidth(), identifier.getHeight());
        matrices.pop();
    }
}
