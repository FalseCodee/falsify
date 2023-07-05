package falsify.falsify.utils.config.translators;

import com.google.gson.JsonObject;
import falsify.falsify.module.DisplayModule;

public class DisplayModuleTranslator {
    public static JsonObject translateDisplayModule(DisplayModule<?> dm) {
        JsonObject json = new JsonObject();
        json.addProperty("x", dm.getRenderModule().getX());
        json.addProperty("y", dm.getRenderModule().getY());
        json.addProperty("scale", dm.getRenderModule().getScale());
        return json;
    }

    public static void loadDisplayModule(DisplayModule<?> dm, JsonObject json) {
        dm.getRenderModule().setX(json.getAsJsonPrimitive("x").getAsDouble());
        dm.getRenderModule().setY(json.getAsJsonPrimitive("y").getAsDouble());
        dm.getRenderModule().setScale(json.getAsJsonPrimitive("scale").getAsDouble());
    }
}
