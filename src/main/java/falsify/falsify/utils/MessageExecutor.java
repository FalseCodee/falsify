package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.text.Text;

import java.util.concurrent.ScheduledExecutorService;

public class MessageExecutor {
    private final String message;
    private final long delay;
    private final long period;

    public MessageExecutor(String message, long delay) {
        this.message = message;
        this.delay = delay;
        this.period = delay;
    }
    public MessageExecutor(String message, long delay, long period) {
        this.message = message;
        this.delay = delay;
        this.period = period;
    }

    public void runTaskLater(){
        new FalseRunnable() {
            @Override
            public void run() {
                ChatModuleUtils.sendMessage(message, false);
            }
        }.runTaskLater(delay);
    }
    public ScheduledExecutorService runTaskTimer() {
        return new FalseRunnable() {
            @Override
            public void run() {
                Falsify.mc.player.sendMessage(Text.of(message));
            }
        }.runTaskTimer(delay, period);
    }
}
