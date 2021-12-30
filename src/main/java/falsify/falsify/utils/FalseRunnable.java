package falsify.falsify.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class FalseRunnable implements Runnable{

    public void runTaskLater(long delay) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this, delay, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    public ScheduledExecutorService runTaskTimer(long delay, long period) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this, delay, period, TimeUnit.MILLISECONDS);
        return executor;
    }
}
