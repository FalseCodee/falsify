package falsify.falsify.automation.types;

import falsify.falsify.automation.Automation;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class GameMessageReceivedAutomation extends Automation {
    public GameMessageReceivedAutomation(String name, int keyCode) {
        super(name, "Ran when a message is received in chat.", keyCode);
        setShouldEnable(event -> event instanceof EventPacketRecieve
                && ((EventPacketRecieve) event).getPacket() instanceof GameMessageS2CPacket packet);
        setSingleUse(false);
    }

    @Override
    public void run(Event<?> event) {
        if(event instanceof EventPacketRecieve
                && ((EventPacketRecieve) event).getPacket() instanceof GameMessageS2CPacket packet) {
            String message = ChatModuleUtils.concatArray(packet.content().withoutStyle(), "");
            setValue("message", message);
        }
        super.run(event);
    }
}
