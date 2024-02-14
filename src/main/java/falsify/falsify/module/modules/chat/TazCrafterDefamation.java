package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EntityDeathEvent;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.MessageExecutor;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;


import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

public class TazCrafterDefamation extends Module {

    public TazCrafterDefamation() {
        super("EZ", "TAZCRAFTER EZ EZ EZ EZ EZ", false, Category.MISC, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!(event instanceof EntityDeathEvent playerDeathEvent)) return;


        Entity attacker = playerDeathEvent.getSource().getAttacker();
        Entity victim = playerDeathEvent.getVictim();
        mc.player.sendMessage(Text.of("death triggered"), false);
        if(attacker != null) mc.player.sendMessage(Text.of("a: " + attacker.getDisplayName().getString()), false);
        if(victim != null) mc.player.sendMessage(Text.of("v: " + victim.getDisplayName().getString()), false);


        if(attacker == null || victim == null || mc.player == null) return;
        mc.player.sendMessage(Text.of(attacker.getDisplayName().getString() + " killed " + victim.getDisplayName().getString()), false);

        if(attacker.getUuid() != mc.player.getUuid()) return;


//        FalseRunnable.of(() -> ChatModuleUtils.sendMessage(victim.getDisplayName().getString() + " DOWN!!! EZ!! L!! LOOOOL!!! EZ!! LOOOOOOOOOOL! BOZO DOWN!!!", false)).runTaskLater(200);

    }
}
