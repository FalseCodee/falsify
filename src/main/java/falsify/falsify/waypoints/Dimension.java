package falsify.falsify.waypoints;

public enum Dimension {
    OVERWORLD(0),
    NETHER(1),
    END(2);

    private final int index;
    Dimension(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
