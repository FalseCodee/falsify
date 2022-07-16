package falsify.falsify.utils;

import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

import java.net.UnknownHostException;

public class NetworkUtils {
    public static void ping(String address, PingRunnable runnable) {
        ServerInfo serverInfo = new ServerInfo("Minecraft Server", address, false);
        serverInfo.online = true;
        serverInfo.ping = -2L;
        serverInfo.label = Text.empty();
        serverInfo.playerCountLabel = Text.empty();
        try {
            new MultiplayerServerListPinger().add(serverInfo, () -> runnable.run(serverInfo));
        } catch (UnknownHostException var2) {
            serverInfo.ping = -1L;
            serverInfo.label = Text.of("Cannot Resolve");
            runnable.run(serverInfo);
        } catch (Exception var3) {
            serverInfo.ping = -1L;
            serverInfo.label = Text.of("Cannot Connect");
            runnable.run(serverInfo);
        }
        serverInfo.ping = -1L;
        serverInfo.label = Text.of("Cannot Connect");
        System.out.println("Do I get here?");
    }

    @FunctionalInterface
    public interface PingRunnable {
        void run(ServerInfo serverInfo);
    }
}
