package falsify.falsify.utils;

import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

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

    public static String postRequest(String urlString, String jsonInput) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    @FunctionalInterface
    public interface PingRunnable {
        void run(ServerInfo serverInfo);
    }
}
