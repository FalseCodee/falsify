package falsify.falsify.module;

import java.awt.*;

public enum Category {
    MOVEMENT("Movement", new Color(6, 44, 0)),
    COMBAT("Combat", new Color(0, 52, 38)),
    MISC("Misc", new Color(0, 54, 59)),
    PLAYER("Player", new Color(0, 41, 44)),
    RENDER("Render", new Color(0, 26, 54));

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
