package falsify.falsify.module;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class ChatModule extends Module {
    public String name;
    public boolean toggled;
    public int keyCode;
    public Category category;
    public static MinecraftClient mc = MinecraftClient.getInstance();

    public ChatModule(String name, Category category, int keyCode){
        super(name, category, keyCode);
        this.name = name;
        this.category = category;
        this.keyCode = keyCode;

    }
    public ChatModule(String name, Category category, int keyCode, boolean enabled){
        super(name, category, keyCode, enabled);
        this.name = name;
        this.category = category;
        this.keyCode = keyCode;
        if(enabled){
            this.toggle();
        }
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void toggle(){
        this.toggled = !this.toggled;
        if(toggled){
            ModuleManager.enabledModules.add(this);
            onEnable();
        } else{
            ModuleManager.enabledModules.remove(this);
            onDisable();
        }
    }
    public void onEnable(){

    }
    public void onDisable(){

    }
    public void onEvent(Event e){
        if(e instanceof EventPacketRecieve){
            if(((EventPacketRecieve) e).getPacket() instanceof net.minecraft.network.packet.s2c.play.GameMessageS2CPacket){
                onChat(((GameMessageS2CPacket) ((EventPacketRecieve) e).getPacket()).getMessage().getString());
            }
        }
    }
    public void onChat(String message){

    }
}
