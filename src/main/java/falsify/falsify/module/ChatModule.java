package falsify.falsify.module;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class ChatModule extends Module {
    public ChatModule(String name, Category category, int keyCode){
        super(name, category, keyCode);
    }
    public ChatModule(String name, Category category, int keyCode, boolean enabled){
        super(name, category, keyCode, enabled);
    }



    public void onEvent(Event e){
        if(e instanceof EventPacketRecieve){
            if(((EventPacketRecieve) e).getPacket() instanceof net.minecraft.network.packet.s2c.play.GameMessageS2CPacket){
                onChat(((EventPacketRecieve) e), ((GameMessageS2CPacket) ((EventPacketRecieve) e).getPacket()).getMessage().getString());
            }
        }
    }
    public void onChat(EventPacketRecieve eventPacketRecieve, String message){

    }
}
