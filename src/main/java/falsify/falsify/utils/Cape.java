package falsify.falsify.utils;

import falsify.falsify.Falsify;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Cape {
    private LegacyIdentifier capeId;
    private final String capeName;
    public Cape(String capeName) {
        this.capeName = capeName;
    }

    private void setCape() {
        CompletableFuture<LegacyIdentifier> cape = Falsify.textureCacheManager.getIdentifier(capeName);
        if(cape.isDone()) {
            capeId = cape.getNow(null);
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
    }
}
