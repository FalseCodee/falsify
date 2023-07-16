package falsify.falsify.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonHelper {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static JsonObject fromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();

            return JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<JsonObject> futureJsonUrl(String urlString) {
        return CompletableFuture.supplyAsync(() -> fromUrl(urlString), executor);
    }
}
