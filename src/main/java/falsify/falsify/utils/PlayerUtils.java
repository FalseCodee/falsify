package falsify.falsify.utils;

import falsify.falsify.Falsify;
import falsify.falsify.waypoints.Dimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.concurrent.CompletableFuture;

public class PlayerUtils {
    public static void leftClick(long duration) {
        Falsify.mc.options.attackKey.setPressed(true);
        new FalseRunnable() {
            @Override
            public void run() {
                Falsify.mc.options.attackKey.setPressed(false);
            }
        }.runTaskLater(duration);
    }

    public static void rightClick(long duration) {
        Falsify.mc.options.useKey.setPressed(true);
        new FalseRunnable() {
            @Override
            public void run() {
                Falsify.mc.options.useKey.setPressed(false);
            }
        }.runTaskLater(duration);
    }

    public static boolean isFarmable(Item item) {
        return (item == Items.WHEAT_SEEDS) ||
                (item == Items.CARROT) ||
                (item == Items.POTATO) ||
                (item == Items.BEETROOT_SEEDS) ||
                (item == Items.PUMPKIN_SEEDS) ||
                (item == Items.MELON_SEEDS);
    }

    public static Dimension getDimension() {
        if (Falsify.mc.world == null) return Dimension.OVERWORLD;

        return switch (Falsify.mc.world.getRegistryKey().getValue().getPath()) {
            case "the_nether" -> Dimension.NETHER;
            case "the_end" -> Dimension.END;
            default -> Dimension.OVERWORLD;
        };
    }

    public static void teleport(Vec3d target) {
        Falsify.mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(target.x, target.y, target.z, Falsify.mc.player.getYaw(), Falsify.mc.player.getPitch(), Falsify.mc.player.isOnGround()));
        Falsify.mc.player.setPosition(target);
    }

    public static int TP_EXPLOIT_MAX_RANGE = 15*10;


    public static void tpExploit(Vec3d target) {
        sendTpExploitPackets(Falsify.mc.player, target, (int) Falsify.mc.player.getPos().distanceTo(target) / 10 + 1);
        Falsify.mc.player.setPosition(target);
    }

    public static void tpExploit(Vec3d target, int packets) {
        sendTpExploitPackets(Falsify.mc.player, target, packets);
        Falsify.mc.player.setPosition(target);
    }

    public static CompletableFuture<Void> longTpExploit(Vec3d target) {
        CompletableFuture<Void> future = new CompletableFuture<>();
            new FalseRunnable() {
                @Override
                public void run() {
                    Vec3d pos = Falsify.mc.player.getPos();
                    Vec3d direction = target.subtract(pos).normalize();
                    Vec3d jumpPos = pos;
                    if(jumpPos.x != target.x) jumpPos = jumpPos.add(direction.x * TP_EXPLOIT_MAX_RANGE, 0, 0);
                    if(jumpPos.y != target.y) jumpPos = jumpPos.add(0, direction.y * TP_EXPLOIT_MAX_RANGE, 0);
                    if(jumpPos.z != target.z) jumpPos = jumpPos.add(0, 0, direction.z * TP_EXPLOIT_MAX_RANGE);

                    if((pos.x < target.x && jumpPos.x > target.x) || (pos.x > target.x && jumpPos.x < target.x)) {
                        jumpPos = new Vec3d(target.x, jumpPos.y, jumpPos.z);
                    }
                    if((pos.y < target.y && jumpPos.y > target.y) || (pos.y > target.y && jumpPos.y < target.y)) {
                        jumpPos = new Vec3d(jumpPos.x, target.y, jumpPos.z);
                    }
                    if((pos.z < target.z && jumpPos.z > target.z) || (pos.z > target.z && jumpPos.z < target.z)) {
                        jumpPos = new Vec3d(jumpPos.x, jumpPos.y, target.z);
                    }
                    jumpPos = findSafeBlockNearLocation(Falsify.mc.world, Falsify.mc.player, jumpPos);
                    if(jumpPos == null) jumpPos = Falsify.mc.player.getPos();
                    tpExploit(jumpPos);

                    if(!jumpPos.equals(target)) {
                        longTpExploit(target).whenComplete((a, b) -> future.complete(null));
                    } else {
                        future.complete(null);
                    }
                }
            }.runTaskNextTick();
            return future;
    }

    public static CompletableFuture<Void> tpExploitWithPathfinding(Vec3d from, Vec3d to) {
        to = findSafeBlockNearLocation(Falsify.mc.world, Falsify.mc.player, to);
        if(to == null) return null;
        double topY = (from.y > 80) ? 320 : -68;
        //teleport up
        Vec3d fromUpPos = new Vec3d(from.x, topY, from.z);
        Vec3d toUpPos = new Vec3d(to.x, topY, to.z);
        Vec3d toPos = to;
        int i = 0;
        CompletableFuture<Void> future = new CompletableFuture<>();
        Falsify.mc.player.setPosition(from);
        longTpExploit(fromUpPos)
                .thenAccept((a) ->
                        longTpExploit(toUpPos)
                                .thenAccept((a1) ->
                                        longTpExploit(toPos)
                                                .thenAccept((a2) -> future.complete(null))));
        return future;
    }

    public static void sendTpExploitPackets(Entity entity, Vec3d from, Vec3d target, int packets) {
        for (int i = 0; i < packets; i++) {
            Falsify.mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(from.getX(), from.getY(), from.getZ(), true));
        }
        Falsify.mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(target.x, target.y, target.z, entity.getYaw(), entity.getPitch(), true));
    }

    public static void sendTpExploitPackets(Entity entity, Vec3d target, int packets) {
        sendTpExploitPackets(entity, entity.getPos(), target, packets);
    }

    public static HitResult findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
        double d = Math.max(blockInteractionRange, entityInteractionRange);
        double e = MathHelper.square(d);
        Vec3d vec3d = camera.getCameraPosVec(tickDelta);
        HitResult hitResult = camera.raycast(d, tickDelta, false);
        double f = hitResult.getPos().squaredDistanceTo(vec3d);
        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3d vec3d2 = camera.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        float g = 1.0F;
        Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);
        return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < f
                ? ensureTargetInRange(entityHitResult, vec3d, entityInteractionRange)
                : ensureTargetInRange(hitResult, vec3d, blockInteractionRange);
    }

    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {
        Vec3d vec3d = hitResult.getPos();
        if (!vec3d.isInRange(cameraPos, interactionRange)) {
            Vec3d vec3d2 = hitResult.getPos();
            Direction direction = Direction.getFacing(vec3d2.x - cameraPos.x, vec3d2.y - cameraPos.y, vec3d2.z - cameraPos.z);
            return BlockHitResult.createMissed(vec3d2, direction, BlockPos.ofFloored(vec3d2));
        } else {
            return hitResult;
        }
    }

    public static Vec3d safeLocationAboveBlock(World world, BlockPos blockPos) {
        int i = 0;
        while (world.getBlockState(blockPos).getCollisionShape(world, blockPos) == VoxelShapes.empty() && i < 90) {
            blockPos = blockPos.up();
            i++;
        }

        BlockPos gap = blockPos.up();
        while ((world.getBlockState(gap).getCollisionShape(world, blockPos) != VoxelShapes.empty()
                || world.getBlockState(gap.up()).getCollisionShape(world, blockPos) != VoxelShapes.empty()) && i < 90) {
            gap = gap.up();
            i++;
        }

        if (i >= 90) {
            return null;
        }

        return gap.toCenterPos().subtract(0, 0.5, 0);
    }

    public static Vec3d safeLocationBelowBlock(World world, BlockPos blockPos) {
        int i = 0;
        while (world.getBlockState(blockPos).getCollisionShape(world, blockPos) == VoxelShapes.empty() && i < 90) {
            blockPos = blockPos.down();
            i++;
        }

        BlockPos gap = blockPos.down();
        while ((world.getBlockState(gap).getCollisionShape(world, blockPos) != VoxelShapes.empty()
                || world.getBlockState(gap.down()).getCollisionShape(world, blockPos) != VoxelShapes.empty()) && i < 90) {
            gap = gap.down();
            i++;
        }

        if (i >= 90) {
            return null;
        }

        return gap.down().toCenterPos().subtract(0, 0.5, 0);
    }

    public static Vec3d findSafeBlockNearLocation(World world, PlayerEntity player, Vec3d location) {
        Vec3d fixes = new Vec3d(0, 0, 0);
        if(location.y >= 319 || location.y <= -66) return location;
        if(player.getPos().subtract(location).horizontalLength() > Falsify.mc.options.getClampedViewDistance()*16) return BlockPos.ofFloored(location).toCenterPos().subtract(0, 0.5, 0);;

        fixes = fixes.add(pushOutOfBlocks(player, new Vec3d(location.getX() - (double) player.getWidth() * 0.35, location.y, location.getZ() + (double) player.getWidth() * 0.35)));
        fixes = fixes.add(pushOutOfBlocks(player, new Vec3d(location.getX() - (double) player.getWidth() * 0.35, location.y, location.getZ() - (double) player.getWidth() * 0.35)));
        fixes = fixes.add(pushOutOfBlocks(player, new Vec3d(location.getX() + (double) player.getWidth() * 0.35, location.y, location.getZ() - (double) player.getWidth() * 0.35)));
        fixes = fixes.add(pushOutOfBlocks(player, new Vec3d(location.getX() + (double) player.getWidth() * 0.35, location.y, location.getZ() + (double) player.getWidth() * 0.35)));

        location = location.add(fixes);

        BlockPos posAbove = BlockPos.ofFloored(location);
        BlockPos posBelow = posAbove;

        for (int i = 0; i < 50; i++) {
            if (world.getBlockState(posAbove).getCollisionShape(world, posAbove) == VoxelShapes.empty()
                    && world.getBlockState(posAbove.up()).getCollisionShape(world, posAbove.up()) == VoxelShapes.empty()
                    && player.getPos().distanceTo(posAbove.toCenterPos()) < TP_EXPLOIT_MAX_RANGE) {

                return posAbove.toCenterPos().subtract(0, 0.5, 0);
            } else if (world.getBlockState(posBelow).getCollisionShape(world, posBelow) == VoxelShapes.empty()
                    && world.getBlockState(posBelow.down()).getCollisionShape(world, posBelow.down()) == VoxelShapes.empty()
                    && player.getPos().distanceTo(posBelow.down().toCenterPos()) < TP_EXPLOIT_MAX_RANGE) {

                return posBelow.down().toCenterPos().subtract(0, 0.5, 0);
            }
            posBelow = posBelow.down();
            posAbove = posAbove.up();
        }
        return null;
    }

    public static Vec3d pushOutOfBlocks(PlayerEntity player, Vec3d pos) {
        BlockPos blockPos = BlockPos.ofFloored(pos.x, pos.y, pos.z);
        if (wouldCollideAt(player, blockPos)) {
            double d = pos.x - (double) blockPos.getX();
            double e = pos.z - (double) blockPos.getZ();
            Direction direction = null;
            double f = Double.MAX_VALUE;
            Direction[] directions = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};

            for (Direction direction2 : directions) {
                double g = direction2.getAxis().choose(d, 0.0, e);
                double h = direction2.getDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - g : g;
                if (h < f && !wouldCollideAt(player, blockPos.offset(direction2))) {
                    f = h;
                    direction = direction2;
                }
            }

            if (direction != null) {
                if (direction.getAxis() == Direction.Axis.X) {
                    return new Vec3d(0.25 * (double) direction.getOffsetX(), 0, 0);
                } else {
                    return new Vec3d(0, 0, 0.25 * (double) direction.getOffsetZ());
                }
            }
        }
        return new Vec3d(0, 0, 0);
    }

    public static boolean wouldCollideAt(PlayerEntity player, BlockPos pos) {
        Box box = player.getBoundingBox();
        Box box2 = new Box(pos.getX(), box.minY, pos.getZ(), (double) pos.getX() + 1.0, box.maxY, (double) pos.getZ() + 1.0).contract(1.0E-7);
        return player.getWorld().canCollide(player, box2);
    }
}
