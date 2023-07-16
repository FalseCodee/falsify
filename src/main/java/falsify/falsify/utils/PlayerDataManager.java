package falsify.falsify.utils;

import com.google.gson.JsonObject;

import java.util.UUID;

public class PlayerDataManager {
    private JsonObject playerData;

    public PlayerDataManager() {
        new FalseRunnable() {
            @Override
            public void run() {
                init(JsonHelper.fromUrl("https://raw.githubusercontent.com/FalseCodee/legacy-client-assets/main/data.json"));
            }
        }.runTaskAsync();
    }

    public void init(JsonObject data) {
        playerData = data.getAsJsonObject("playerdata");
    }

    public JsonObject getPlayerObject(UUID uuid) {
        String uuidString = uuid.toString();
        return playerData.getAsJsonObject(uuidString);
    }

    public String getCape(UUID uuid) {
        JsonObject playerObject = getPlayerObject(uuid);
        if(playerObject == null) return null;
        return playerObject.getAsJsonPrimitive("cape").getAsString();
    }

    public boolean hasCape(UUID uuid) {
        JsonObject playerObject = getPlayerObject(uuid);
        if(playerObject == null) return false;
        return playerObject.has("cape");
    }
}
