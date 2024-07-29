package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkUtils {
    public static void ping(String address, PingRunnable runnable) {
        ServerInfo serverInfo = new ServerInfo("Minecraft Server", address, ServerInfo.ServerType.OTHER);
        serverInfo.ping = -2L;
        serverInfo.label = Text.empty();
        serverInfo.playerCountLabel = Text.empty();
        try {
            new MultiplayerServerListPinger().add(serverInfo, () -> runnable.run(serverInfo), () -> Falsify.logger.warn("Ping Callback: " + serverInfo.address));
        } catch (Exception var2) {
            serverInfo.ping = -1L;
            serverInfo.label = Text.of("Cannot Connect");
            runnable.run(serverInfo);
        }
    }

    public static String postRequest(String urlString, String jsonInput) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, jsonInput);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(String urlString, String jsonInput) throws IOException {
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
        return connection;
    }

    @FunctionalInterface
    public interface PingRunnable {
        void run(ServerInfo serverInfo);
    }
}
