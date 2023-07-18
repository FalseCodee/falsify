package falsify.falsify.utils.config.translators;

import com.google.gson.JsonObject;
import falsify.falsify.gui.clickgui.primatives.Tab;

import java.util.List;

public class ClickGuiTranslator {
    public static JsonObject translateClickGui(List<Tab> tabs) {
        JsonObject json = new JsonObject();
        for(Tab tab : tabs) {
            JsonObject object = new JsonObject();
            object.addProperty("x", tab.getX());
            object.addProperty("y", tab.getY());
            object.addProperty("extended", tab.isExtended());
            json.add(tab.getCategory().getName(), object);
        }
        return json;
    }

    public static void loadClickGui(List<Tab> tabs, JsonObject json) {
        for(Tab tab : tabs) {
            JsonObject object = json.getAsJsonObject(tab.getCategory().getName());
            tab.setX(object.getAsJsonPrimitive("x").getAsDouble());
            tab.setY(object.getAsJsonPrimitive("y").getAsDouble());
            tab.setExtended(object.getAsJsonPrimitive("extended").getAsBoolean());
        }
    }
}
