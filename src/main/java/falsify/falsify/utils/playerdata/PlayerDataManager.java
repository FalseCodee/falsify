package falsify.falsify.utils.playerdata;

import com.google.gson.JsonObject;
import falsify.falsify.Falsify;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.JsonHelper;
import falsify.falsify.utils.playerdata.PlayerData;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerDataManager {
    private JsonObject playerData;
    private ArrayList<PlayerData> dataList;

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
        dataList = new ArrayList<>();
        for(String key : playerData.keySet()) {
            dataList.add(PlayerData.fromUuid(UUID.fromString(key)));
        }
        Falsify.logger.info("Loaded Player Data");
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

    public ArrayList<PlayerData> getDataList() {
        return dataList;
    }
}
