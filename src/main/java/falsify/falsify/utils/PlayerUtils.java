package falsify.falsify.utils;

import falsify.falsify.Falsify;

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
}
