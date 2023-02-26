package falsify.falsify.module;

public enum Category {
    MOVEMENT("Movement"),
    COMBAT("Combat"),
    MISC("Misc"),
    PLAYER("Player"),
    RENDER("Render");

    private final String name;
    Category(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
