package falsify.falsify.utils.capes;

import falsify.falsify.Falsify;
import falsify.falsify.utils.LegacyIdentifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Cape {
    private LegacyIdentifier capeId;
    private final String capeName;
    public Cape(String capeName) {
        this.capeName = capeName;
    }

    protected void setCape() {
        if(capeId == null) {
            capeId = Falsify.textureCacheManager.getIdentifier(capeName);
        }
    }

    @Nullable
    public LegacyIdentifier getCape() {
        if(capeId == null) setCape();
        return capeId;
    }

    public String getCapeName() {
        return capeName;
    }

    public static final ArrayList<Cape> capes = new ArrayList<>();

    public static Cape getCape(String name) {
        return capes.stream().filter(cape -> cape.getCapeName().equals(name)).findFirst().orElse(null);
    }

    public static void addCapes() {
        capes.add(new Cape("dev_cape"));
        capes.add(new Cape("armorup_cape"));
        capes.add(new GifCape("sus_cape", "https://cdn.discordapp.com/attachments/755141818743652444/1140435807157616691/image.gif"));
    }
}
