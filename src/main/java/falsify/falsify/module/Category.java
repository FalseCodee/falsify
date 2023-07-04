package falsify.falsify.module;

import java.awt.*;

public enum Category {
    MOVEMENT("Movement", new Color(199, 229, 255)),
    COMBAT("Combat", new Color(128, 230, 255)),
    MISC("Misc", new Color(39, 201, 255)),
    PLAYER("Player", new Color(0, 164, 255)),
    RENDER("Render", new Color(0, 130, 255));

    private final String name;
    private final Color color;
    Category(String name, Color color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }
    public Color getColor() {
        return this.color;
    }
}
