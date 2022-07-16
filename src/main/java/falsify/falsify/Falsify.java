package falsify.falsify;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.lwjgl.glfw.GLFW;

public class Falsify {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Session session;
    public static void init(Session session) {
        ModuleManager.init();
        Falsify.session = session;
    }

    public static void onEvent(Event e){
        if(e instanceof EventPacketRecieve || e instanceof EventPacketSend){
            for(Module module : ModuleManager.enabledModules){
                module.onEvent(e);
            }
        } else {
            if(mc.player == null)
                return;
            if(e instanceof EventKey){
                for(Module module : ModuleManager.modules){
                    if(((EventKey) e).getKey() == module.getKeyCode() && ((EventKey) e).getAction() == GLFW.GLFW_PRESS){
                        module.toggle();
                        module.onEvent(e);
                    }
                }
            } else {
                for(Module module : ModuleManager.enabledModules){
                    module.onEvent(e);
                }
            }
        }
    }
}
