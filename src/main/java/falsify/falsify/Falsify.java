package falsify.falsify;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.TextureCacheManager;
import falsify.falsify.utils.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.lwjgl.glfw.GLFW;

import java.io.File;


public class Falsify{
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Session session;
    public static TextureCacheManager textureCacheManager;
    public static ConfigManager configManager;
    public static File clientDir;
    public static final String title = "Legacy Client";
    public static void init(Session session) {
        Falsify.session = session;
        String clientDirPath = mc.runDirectory.getAbsolutePath()+"\\legacy_client";
        clientDir = new File(clientDirPath);
        clientDir.mkdirs();
        Falsify.textureCacheManager = new TextureCacheManager();
        Falsify.configManager = new ConfigManager();
        ModuleManager.init();
    }

    public static void onEvent(Event e){
        if(e instanceof EventPacketRecieve || e instanceof EventPacketSend){
            for(Module module : ModuleManager.enabledModules){
                module.onEvent(e);
            }
        } else {
            if(mc.player == null)
                return;
            if(e instanceof EventKey ek){
                if(ek.getKey() != -1) {
                    for (Module module : ModuleManager.modules) {
                        if (ek.getKey() == module.getKeyCode() && ek.getAction() == GLFW.GLFW_PRESS) {
                            module.toggle();
                        }
                    }
                }
            }
            for(Module module : ModuleManager.enabledModules){
                module.onEvent(e);
            }

        }
    }

//    @Override
//    public void onInitializeClient() {
//    }
}
