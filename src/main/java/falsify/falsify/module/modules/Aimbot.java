package falsify.falsify.module.modules;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventFrame;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Aimbot extends Module {
    private final RangeSetting range = new RangeSetting("Range", 100, 0, 100, new DecimalFormat("#.#"));
    private final RangeSetting tickSpeed = new RangeSetting("TickSpeed", 0, 0, 100, new DecimalFormat("#"));
    private final RangeSetting speed = new RangeSetting("Speed", 0.25, 0, 1.5, new DecimalFormat("#.##"));
    private final RangeSetting turnSpeed = new RangeSetting("Turn Speed", 0.75, 0, 1.5, new DecimalFormat("#.##"));
    private final ModeSetting type = new ModeSetting("Type", "Players", "All", "Players", "Mobs", "Animals");
    private final ModeSetting sortType = new ModeSetting("Sort By", "Smart", "Distance", "Cursor", "Smart");
    private final BooleanSetting frameSync = new BooleanSetting("Frame Sync", true);
    private final BooleanSetting antiBot = new BooleanSetting("Anti Bot", true);
    private final BooleanSetting hittable = new BooleanSetting("Hittable", true);

    private float timing = 0.0f;

    private final Timer timer = new Timer();
    public static Entity target;
    private Entity oldTarget;

    public Aimbot() {
        super("Aimbot", Category.COMBAT, GLFW.GLFW_KEY_DOWN);
        settings.add(type);
        settings.add(sortType);
        settings.add(range);
        settings.add(speed);
        settings.add(turnSpeed);
        settings.add(tickSpeed);
        settings.add(frameSync);
        settings.add(antiBot);
        settings.add(hittable);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventFrame) {
            if(mc.world == null) return;

            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(LivingEntity.class::isInstance).collect(Collectors.toList());

            switch (type.getMode()) {
                case "All": break;
                case "Players":
                    entityList = entityList.stream().filter(PlayerEntity.class::isInstance).collect(Collectors.toList());
                    if(antiBot.getValue()) entityList = entityList.stream().filter(entity -> ChatModuleUtils.isPlayer(((PlayerEntity)entity).getGameProfile().getId())).collect(Collectors.toList());

                    break;

                case "Mobs": entityList = entityList.stream().filter(HostileEntity.class::isInstance).collect(Collectors.toList()); break;
                case "Animals": entityList = entityList.stream().filter(AnimalEntity.class::isInstance).collect(Collectors.toList()); break;
            }
            if(hittable.getValue()) entityList = entityList.stream().filter(Entity::isAttackable).collect(Collectors.toList());
            entityList = entityList.stream().filter(entity -> (entity.distanceTo(mc.player) < this.range.getValue() && entity != mc.player && entity.isAlive())).collect(Collectors.toList());
            switch (sortType.getMode()){
                case "Distance": entityList.sort(Comparator.comparingDouble(entity -> entity.distanceTo(mc.player))); break;
                case "Cursor": entityList.sort(Comparator.comparingDouble(MathUtils::cursorDistanceTo)); break;
                case "Smart": entityList.sort(Comparator.comparingDouble(entity -> entity.distanceTo(mc.player) + MathUtils.cursorDistanceTo(entity))); break;

            }
            target = null;
            do {
                if (entityList.isEmpty()) {
                    timing = 0.0f;
                    return;
                }

                if (((LivingEntity) entityList.get(0)).getHealth() <= 0) {
                    entityList.remove(0);
                } else {
                    target = entityList.get(0);
                }
            } while (target == null);

            if (this.timer.hasTimeElapsed(tickSpeed.getValue().longValue(), true)) {
                if (this.oldTarget != target)
                    this.timing = 0.0F;
                this.timing += 0.015F;
                if (this.timing > 1.1D - this.turnSpeed.getValue())
                    this.timing = (float)(1.100000023841858D - this.turnSpeed.getValue());
                double t = (this.timing / (1.1D - this.turnSpeed.getValue()));
                if (t > this.speed.getValue())
                    t = this.speed.getValue();
                t = t * t * t * (t * (6.0F * t - 15.0F) + 10.0F);
                mc.player.setYaw(MathUtils.lerp(mc.player.getYaw(), MathUtils.getRotationsNeeded(target)[0], t * ((frameSync.getValue()) ? mc.getLastFrameDuration() : 1)));
                if (mc.crosshairTarget.getType() != HitResult.Type.ENTITY || ((EntityHitResult)mc.crosshairTarget).getEntity() != target)
                    mc.player.setPitch(MathUtils.lerp(mc.player.getPitch(), MathUtils.getRotationsNeeded(target)[1], (t / 2.0F * ((frameSync.getValue()) ? mc.getLastFrameDuration() : 1))));
                this.oldTarget = target;
            }
        }
    }
}
