package falsify.falsify.utils.config.translators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import falsify.falsify.Falsify;
import falsify.falsify.waypoints.Waypoint;

import java.util.List;

public class WaypointTranslator {
    public static JsonArray translateWaypoints(List<Waypoint> waypoints) {
        JsonArray jsonArray = new JsonArray();
        for(Waypoint waypoint : waypoints) {
            jsonArray.add(ModuleTranslator.translateModule(waypoint));
        }
        return jsonArray;
    }

    private static void loadWaypoint(JsonObject jsonObject) {
        Waypoint waypoint = new Waypoint("", 0,0,0);
        Falsify.waypointManager.getWaypoints().add(waypoint);
        ModuleTranslator.loadModule(waypoint, jsonObject);
    }

    public static void loadWaypoints(JsonArray jsonArray) {
        Falsify.waypointManager.getWaypoints().clear();
        Falsify.waypointManager.enabledWaypoints.clear();
        for(JsonElement jsonElement : jsonArray.asList()) {
            assert jsonElement instanceof JsonObject;

            loadWaypoint((JsonObject) jsonElement);
        }
    }
}
