package falsify.falsify.module.modules.movement;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventMouse;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.utils.PlayerUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class PointTeleport extends Module {
    private final BooleanSetting teleportMiss = new BooleanSetting("Teleport on miss", false);
    public PointTeleport() {
        super("Point Teleport", "Point and click to teleport!", true, Category.MOVEMENT, -1);
        settings.add(teleportMiss);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventMouse eventMouse && eventMouse.button == 0 && eventMouse.action == 1 && mc.currentScreen == null) {
            HitResult crosshairTarget = PlayerUtils.findCrosshairTarget(mc.player, 85, 0, Falsify.mc.getRenderTickCounter().getTickDelta(true));
            if(crosshairTarget.getType() == HitResult.Type.MISS && !teleportMiss.getValue()) return;

            if(crosshairTarget instanceof BlockHitResult blockHitResult) {
                BlockPos hitPosition = blockHitResult.getBlockPos();
                VoxelShape collisionShape = Falsify.mc.world.getBlockState(hitPosition).getCollisionShape(mc.world, hitPosition);

                while (collisionShape.isEmpty()) {
                    hitPosition = hitPosition.down();
                    collisionShape = Falsify.mc.world.getBlockState(hitPosition).getCollisionShape(mc.world, hitPosition);
                }

                Box blockHitbox = collisionShape.getBoundingBox();
                double blockHeight = blockHitbox.maxY;

                Vec3d pos = hitPosition.toCenterPos().add(0, blockHeight - 0.5, 0);
                PlayerUtils.tpExploit(pos);
            }
        }
    }
}
