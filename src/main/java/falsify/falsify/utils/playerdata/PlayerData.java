package falsify.falsify.utils.playerdata;

import com.google.gson.JsonObject;
import falsify.falsify.Falsify;
import falsify.falsify.utils.JsonHelper;
import falsify.falsify.utils.LegacyIdentifier;

import java.util.UUID;

public class PlayerData {
    private final String userName;
    private final UUID uuid;
    private final LegacyIdentifier bodyRender;

    private PlayerData(String userName, UUID uuid, LegacyIdentifier bodyRender) {
        this.userName = userName;
        this.uuid = uuid;
        this.bodyRender = bodyRender;
    }

    public String getUserName() {
        return userName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LegacyIdentifier getBodyRender() {
        return bodyRender;
    }

    public static PlayerData fromUuid(UUID uuid) {
        JsonObject json = JsonHelper.fromUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
        if(json == null || !json.has("name")) throw new RuntimeException("Failed to load playerdata: " + uuid);
        String name = json.getAsJsonPrimitive("name").getAsString();
        LegacyIdentifier id = Falsify.textureCacheManager.cacheTextureFromUrl(uuid.toString(), "https://crafatar.com/renders/body/"+uuid+"?overlay=true", false);
        return new PlayerData(name, uuid, id);
    }
}
