package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.RenderHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class Notifications extends Module {
    private static final ArrayList<String> notifications = new ArrayList<>();
    private static final RangeSetting duration = new RangeSetting("Duration", 5000, 1, 10000, 10);
    public Notifications() {
        super("Notifications", "Shows Client Notifications", false, Category.RENDER, -1);
        settings.add(duration);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender e) {
            if(notifications.size() == 0) return;

            MatrixStack matrices = e.getDrawContext().getMatrices();
            matrices.push();
            matrices.translate(RenderHelper.WINDOW.getScaledWidth()-110, RenderHelper.WINDOW.getScaledHeight()-40, 0);
            for(String msg : notifications) {
                RenderHelper.drawSmoothRect(Falsify.theme.primaryColor().darker(), matrices, -2, -2, 100+2, 30+2, 5, new int[] {10, 10, 10, 10});
                RenderHelper.drawSmoothRect(Falsify.theme.primaryColor(), matrices, 0, 0, 100, 30, 3, new int[] {10, 10, 10, 10});

                Falsify.fontRenderer.drawCenteredString(e.getDrawContext(), "Notification", 50, 1, Falsify.theme.secondaryTextColor(), true);
                Falsify.fontRenderer.drawCenteredString(e.getDrawContext(), msg, 50, 12, Falsify.theme.primaryTextColor(), true);
                matrices.translate(0, -37, 0);
            }


            matrices.pop();
        }
    }

    public static void addNotification(String notification) {
        notifications.add(0, notification);
        new FalseRunnable() {
            @Override
            public void run() {
                notifications.remove(notification);
            }
        }.runTaskLater(duration.getValue().longValue());
    }
}
