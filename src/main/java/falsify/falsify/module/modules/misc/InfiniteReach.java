package falsify.falsify.module.modules.misc;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.PlayerUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class InfiniteReach extends Module {
    private final RangeSetting range = new RangeSetting("Range", 160, 4, 160, 1);
    private final BooleanSetting teleport = new BooleanSetting("Teleport near", false);
    private final Timer timer = new Timer();
    private boolean Lheld = false;
    private boolean Rheld = false;
    public InfiniteReach() {
        super("Infinite Reach", "Super long arms!", true, Category.MISC, -1);
        settings.add(range);
        settings.add(teleport);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventUpdate) {
            if(timer.hasTimeElapsed(100, true) && mc.currentScreen == null) {
                int rangeValue = range.getValue().intValue();
                HitResult crosshairTarget = PlayerUtils.findCrosshairTarget(mc.player, rangeValue, rangeValue, Falsify.mc.getRenderTickCounter().getTickDelta(true));
                if(Lheld) handleClick(crosshairTarget, 0);
                else if(Rheld) handleClick(crosshairTarget, 1);
            }
        }
        if(event instanceof EventMouse eventMouse && mc.currentScreen == null) {
            if(eventMouse.action == 1) {
                int rangeValue = range.getValue().intValue();
                HitResult crosshairTarget = PlayerUtils.findCrosshairTarget(mc.player, rangeValue, rangeValue, Falsify.mc.getRenderTickCounter().getTickDelta(true));
                handleClick(crosshairTarget, eventMouse.button);

                timer.reset();
                if(eventMouse.button == 0)      Lheld = true;
                else if(eventMouse.button == 1) Rheld = true;

            } else if(eventMouse.action == 0) {
                if(eventMouse.button == 0)      Lheld = false;
                else if(eventMouse.button == 1) Rheld = false;
            }
        }
    }

    private void handleClick(HitResult crosshairTarget, int button) {
        if(crosshairTarget.getType() == HitResult.Type.MISS) return;
        Vec3d originalPosition = mc.player.getPos();
        double distance = Math.sqrt(originalPosition.squaredDistanceTo(crosshairTarget.getPos()));
        if(mc.crosshairTarget != null && mc.crosshairTarget.getType() != HitResult.Type.MISS) return;

        int packetQuantity = (int) (distance / 10);

        Vec3d fakePosition = generateFakePosition(originalPosition, crosshairTarget.getPos());
        fakePosition = PlayerUtils.findSafeBlockNearLocation(mc.world, mc.player, fakePosition);

        if(fakePosition == null) return;

        if(crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) crosshairTarget;
            if(button == 0) {
                PlayerUtils.tpExploit(fakePosition, packetQuantity);
                mc.interactionManager.attackBlock(bhr.getBlockPos(), bhr.getSide());
                if(!teleport.getValue()) PlayerUtils.teleport(originalPosition);
            } else if (button == 1) {
                PlayerUtils.tpExploit(fakePosition, packetQuantity);
                ActionResult ar = mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), bhr);
                if(!teleport.getValue()) PlayerUtils.teleport(originalPosition);
                if(ar.shouldSwingHand()) mc.player.swingHand(mc.player.getActiveHand());
            }
        } else if(crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult ehr = (EntityHitResult) crosshairTarget;
            if(button == 0) {
                PlayerUtils.tpExploit(fakePosition, packetQuantity);
                mc.interactionManager.attackEntity(mc.player, ehr.getEntity());
                if(!teleport.getValue()) PlayerUtils.teleport(originalPosition);
            } else if (button == 1) {
                PlayerUtils.tpExploit(fakePosition, packetQuantity);
                ActionResult ar = mc.interactionManager.interactEntity(mc.player, ehr.getEntity(), mc.player.getActiveHand());
                if(!teleport.getValue()) PlayerUtils.teleport(originalPosition);
                if(ar.shouldSwingHand()) mc.player.swingHand(mc.player.getActiveHand());
            }
        }
    }

    public Vec3d generateFakePosition(Vec3d from, Vec3d target) {
        return from.add(target.subtract(from).normalize().multiply(from.distanceTo(target)-3));
    }
}
