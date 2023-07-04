package falsify.falsify;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.LegacyIdentifier;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.TextureCacheManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Session;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.InputStream;
import java.net.URL;

public class Falsify{
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Session session;
    public static TextureCacheManager textureCacheManager;
    public static final String title = "Legacy Client";
    public static void init(Session session) {
        ModuleManager.init();
        Falsify.session = session;
        Falsify.textureCacheManager = new TextureCacheManager();
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
