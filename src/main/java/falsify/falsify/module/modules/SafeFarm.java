package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.PlayerUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SafeFarm extends Module {

    RangeSetting dist = new RangeSetting("Distance", 25, 0, 50, 1);
    RangeSetting dur = new RangeSetting("Duration", 200, 0, 1000, 5);

    Timer timer = new Timer();
    public SafeFarm() {
        super("Safe Farm", Category.PLAYER, -1);
        settings.add(dist);
        settings.add(dur);
    }

    public static Vec3d target;
    public boolean plant = false;
    @Override
    public void onEnable() {
        findNextTarget();
    }

    private void findNextTarget() {
        int x = (int) mc.player.getX(); // current position; x
        int z = (int) mc.player.getZ(); // current position; y
        int d = 0; // current direction; 0=RIGHT, 1=DOWN, 2=LEFT, 3=UP
        int s = 1; // chain size


        for (int k=1; k<=(dist.getValue()-1); k++)
        {
            for (int j=0; j<(k<(dist.getValue()-1)?2:3); j++)
            {
                for (int i=0; i<s; i++)
                {
                    BlockState block = mc.world.getBlockState(new BlockPos(x, mc.player.getY()+0.2, z));

                    if(block.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(block)){
                        target = new Vec3d(x+0.5, mc.player.getY()+0.2, z+0.5);
                        plant = false;
                        mc.player.sendMessage(Text.of(target.toString()));
                        return;
                    } else if (block.getBlock() instanceof AirBlock && mc.player.getInventory().main.stream().anyMatch(is -> is.getItem().isFood())){
                        if(!mc.player.getInventory().getMainHandStack().getItem().isFood()) {
                            for (int p = 0; p < 8; p++) {
                                if (mc.player.getInventory().main.get(p).getItem().isFood()) {
                                    mc.player.getInventory().selectedSlot = p;
                                    break;
                                }
                            }
                        }
                        BlockState underBlock = mc.world.getBlockState(new BlockPos(x, mc.player.getY()-0.2, z));
                        if(underBlock.getBlock() instanceof FarmlandBlock) {
                            target = new Vec3d(x+0.5, mc.player.getY()-0.2, z+0.5);
                            plant = true;
                            mc.player.sendMessage(Text.of(target.toString()));
                            return;
                        }
                    }
                    switch (d) {
                        case 0 -> z = z + 1;
                        case 1 -> x = x + 1;
                        case 2 -> z = z - 1;
                        case 3 -> x = x - 1;
                    }
                }
                d = (d+1)%4;
            }
            s = s + 1;
        }
    }

    @Override
    public void onEvent(Event<?> event) {
       if(event instanceof EventUpdate) {
           if(target != null) {
               if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;

               BlockPos block = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
               if (block.equals(new BlockPos(target))) {
//                   ((MixinMinecraft) mc).doAttack();
                   if(plant) PlayerUtils.rightClick(dur.getValue().intValue());
                   else PlayerUtils.leftClick(dur.getValue().intValue());

                   new FalseRunnable() {
                       @Override
                       public void run() {
//                           ((MixinMinecraft) mc).doItemUse();
                           target = null;
                           findNextTarget();
                       }
                   }.runTaskLater(50);
               }
           } else if(timer.hasTimeElapsed(10000, true)) {
               findNextTarget();
           }
       }
    }
}
