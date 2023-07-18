package falsify.falsify.utils.config.translators;

import com.google.gson.JsonObject;
import falsify.falsify.gui.editor.module.Anchor;
import falsify.falsify.module.DisplayModule;

public class DisplayModuleTranslator {
    public static JsonObject translateDisplayModule(DisplayModule<?> dm) {
        JsonObject json = new JsonObject();
        json.addProperty("relx", dm.getRenderModule().getRelativeX());
        json.addProperty("rely", dm.getRenderModule().getRelativeY());
        json.addProperty("anchor", dm.getRenderModule().getAnchor().name());
        json.addProperty("scale", dm.getRenderModule().getScale());
        return json;
    }

    public static void loadDisplayModule(DisplayModule<?> dm, JsonObject json) {
        dm.getRenderModule().setRelativeX(json.getAsJsonPrimitive("relx").getAsDouble());
        dm.getRenderModule().setRelativeY(json.getAsJsonPrimitive("rely").getAsDouble());
        dm.getRenderModule().setAnchor(Anchor.valueOf(json.getAsJsonPrimitive("anchor").getAsString()));
        dm.getRenderModule().setScale(json.getAsJsonPrimitive("scale").getAsDouble());
    }
}
