package falsify.falsify.utils;

import net.minecraft.util.Identifier;

public class LegacyIdentifier {

    private final Identifier id;
    private final int width;
    private final int height;
    public LegacyIdentifier(String path, int width, int height) {
        this.id = Identifier.of("falsify", path);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Identifier getId() {
        return id;
    }
}
