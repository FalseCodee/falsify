package falsify.falsify.module.modules.misc;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.item.Items;

public class DupeDrop extends Module {

    RangeSetting delay = new RangeSetting("Delay", 500, 0, 2000, 10);
    RangeSetting amount = new RangeSetting("Amount", 3, 1, 10, 1);
    Timer timer = new Timer();

    boolean refilling = false;
    int index = 0;
    public DupeDrop() {
        super("DupeDrop", "/dupe items then drops them.", false, Category.MISC, -1);
        settings.add(delay);
        settings.add(amount);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate){
            if(!timer.hasTimeElapsed(delay.getValue().longValue(), true)) return;

            if(refilling) {
                if(mc.player.getInventory().getMainHandStack().getItem() == Items.AIR) {
                    int i = 8;
                    while(mc.player.getInventory().getStack(i).getItem() == Items.AIR && i > 0) {
                        i--;
                    }
                    mc.player.getInventory().selectedSlot = i;
                    index = 0;
                    return;
                }
                if(index > 8/amount.getValue()) {
                    refilling = false;
                    index = 0;
                }
                else {
                    ChatModuleUtils.sendMessage("/dupe " + amount.getValue().intValue(), true);
                    index++;
                }
            } else {
                if(index == 8) {
                    refilling = true;
                    index = 0;
                } else {
                    mc.player.getInventory().selectedSlot = index;
                    mc.player.dropSelectedItem(true);
                    index++;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
    }
}
