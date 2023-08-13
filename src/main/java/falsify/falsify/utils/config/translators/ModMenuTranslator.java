package falsify.falsify.utils.config.translators;

import com.google.gson.JsonObject;
import falsify.falsify.gui.modmenu.primitives.Panel;

public class ModMenuTranslator {
    public static JsonObject translatePanel(Panel panel) {
        JsonObject json = new JsonObject();
        json.addProperty("active", panel.getActiveIndex());
        return json;
    }

    public static void loadPanel(Panel panel, JsonObject json) {
        panel.setActiveIndex(json.getAsJsonPrimitive("active").getAsInt());
    }
}
