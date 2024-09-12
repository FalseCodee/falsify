package falsify.falsify;

import falsify.falsify.gui.modmenu.primitives.Theme;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.*;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.misc.PostProcess;
import falsify.falsify.utils.*;
import falsify.falsify.utils.netty.NettyClient;
import falsify.falsify.utils.playerdata.PlayerDataManager;
import falsify.falsify.utils.config.ConfigManager;
import falsify.falsify.utils.fonts.FontRenderer;
import falsify.falsify.utils.fonts.Fonts;
import falsify.falsify.utils.shaders.ShaderManager;
import falsify.falsify.waypoints.Waypoint;
import falsify.falsify.waypoints.WaypointManager;
import me.falsecode.netty.packet.packets.c2s.ClientDisconnectPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;


public class Falsify {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static Session session;
    public static Logger logger;
    public static TextureCacheManager textureCacheManager;
    public static NettyClient client;
    public static ConfigManager configManager;
    public static PlayerDataManager playerDataManager;
    public static WaypointManager waypointManager;
    public static ShaderManager shaderManager;
    public static PostProcess postProcess;
    public static Theme theme = new Theme(new Color(52, 52, 52), new Color(79, 0, 0), new Color(255, 255, 255), new Color(185, 185, 185));
    public static Fonts fonts;
    public static String DISCORD_WEBHOOK_URL = System.getenv("leg_discord_webhook");

    public static FontRenderer fontRenderer;
    public static File clientDir;
    public static final String title = "Legacy Client";
    public static void init(Session session) {
        Falsify.session = session;
        Falsify.logger = LoggerFactory.getLogger("Legacy Client");
        String clientDirPath = mc.runDirectory.getAbsolutePath()+"\\legacy_client";
        new FalseRunnable() {
            @Override
            public void run() {
                clientDir = new File(clientDirPath);
                clientDir.mkdirs();
                Falsify.logger.info("Created: Client Directory");

                Falsify.textureCacheManager = new TextureCacheManager();
                Falsify.configManager = new ConfigManager();
                Falsify.playerDataManager = new PlayerDataManager();
            }
        }.runTaskAsync();
        fonts = new Fonts();
        fontRenderer = new FontRenderer(fonts.getFonts(), 9, 3);
        ModuleManager.init();
        shaderManager = new ShaderManager();
        waypointManager = new WaypointManager();
        client = new NettyClient("localhost", 8000);
        new FalseRunnable() {
            @Override
            public void run() {
                client.run();
            }
        }.runTaskAsync();
        logger.info("Init Finished");
    }

    public static void shutdown() {
        Falsify.configManager.saveModules();
        Falsify.configManager.saveWaypoints();
        Falsify.configManager.saveConfigFile();
        client.send(new ClientDisconnectPacket());
        client.shutdown();
    }

    public static void onEvent(Event<?> e) {
        if (e instanceof EventPacketRecieve || e instanceof EventPacketSend) {
            for (Module module : ModuleManager.enabledModules) {
                module.onEvent(e);
            }
            return;
        }
        if (mc.player == null)
            return;
        if (e instanceof EventKey ek) {
            if (ek.getKey() != -1) {
                for (Module module : ModuleManager.modules) {
                    if (ek.getKey() == module.getKeyCode() && ek.getAction() == GLFW.GLFW_PRESS) {
                        module.toggle();
                    }
                }
            }
        }
        boolean rendering = e instanceof EventRender || e instanceof EventRender3d;
        if(rendering)
            shaderManager.clear();

        for(Waypoint waypoint : waypointManager.enabledWaypoints) {
            waypoint.onEvent(e);
        }

        for (Module module : ModuleManager.enabledModules) {
            module.onEvent(e);
        }

        if(rendering)
            shaderManager.renderAllCaptures();
    }
}
