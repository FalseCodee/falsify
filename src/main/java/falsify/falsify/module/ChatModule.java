package falsify.falsify.module;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.utils.ChatModuleUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class ChatModule extends Module {
    public ChatModule(String name, String description, Category category, int keyCode){
        super(name, description, false, category, keyCode);
    }
    public ChatModule(String name, String description, Category category, int keyCode, boolean enabled){
        super(name, description, category, keyCode, enabled, false);
    }



    public void onEvent(Event<?> event){
        if(event instanceof EventPacketRecieve){
            if(((EventPacketRecieve) event).getPacket() instanceof net.minecraft.network.packet.s2c.play.GameMessageS2CPacket){
                onChat(((EventPacketRecieve) event), ChatModuleUtils.concatArray(((GameMessageS2CPacket) ((EventPacketRecieve) event).getPacket()).content().withoutStyle(), ""));
            }
        }
    }
    public void onChat(EventPacketRecieve eventPacketRecieve, String message){

    }
}
