package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

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
}
