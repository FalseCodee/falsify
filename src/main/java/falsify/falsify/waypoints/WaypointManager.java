package falsify.falsify.waypoints;

import java.util.ArrayList;

public class WaypointManager {
    private final ArrayList<Waypoint> waypoints = new ArrayList<>();
    public final ArrayList<Waypoint> enabledWaypoints = new ArrayList<>();

    public WaypointManager() {
//        waypoints.add(new Waypoint("test", 0, 100, 0));
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
}
