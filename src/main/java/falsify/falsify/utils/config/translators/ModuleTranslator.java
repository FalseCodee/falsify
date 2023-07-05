package falsify.falsify.utils.config.translators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.*;

import java.util.List;

public class ModuleTranslator {
    public static JsonObject translateModule(Module module) {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", module.isEnabled());
        if(module instanceof DisplayModule<?> dm) json.add("display", DisplayModuleTranslator.translateDisplayModule(dm));
        json.add("settings", translateSettings(module.settings));
        return json;
    }

    private static JsonObject translateSettings(List<Setting<?>> settings){
        JsonObject json = new JsonObject();
        for(int i = 0; i < settings.size(); i++) {
            Setting<?> setting = settings.get(i);
            if(setting instanceof BooleanSetting s) json.addProperty(s.getName(),s.getValue());
            else if(setting instanceof RangeSetting s) json.addProperty(s.getName(),s.getValue());
            else if(setting instanceof ModeSetting s) json.addProperty(s.getName(),s.getIndex());
            else if(setting instanceof KeybindSetting s) json.addProperty(s.getName(),s.getValue());
        }
        return json;
    }

    public static void loadModule(Module module, JsonObject moduleJson) {
        if(moduleJson.getAsJsonPrimitive("enabled").getAsBoolean() != module.toggled) module.toggle();
        if(module instanceof DisplayModule<?> dm) DisplayModuleTranslator.loadDisplayModule(dm, moduleJson.getAsJsonObject("display"));
        loadSettings(module.settings, moduleJson.getAsJsonObject("settings"));
    }


    private static void loadSettings(List<Setting<?>> settings, JsonObject settingsJson){
        for (Setting<?> setting : settings) {
            if(!settingsJson.has(setting.getName())) continue;
            if (setting instanceof BooleanSetting s)
                s.setValue(settingsJson.getAsJsonPrimitive(s.getName()).getAsBoolean());
            else if (setting instanceof RangeSetting s)
                s.setValue(settingsJson.getAsJsonPrimitive(s.getName()).getAsDouble());
            else if (setting instanceof ModeSetting s)
                s.setIndex(settingsJson.getAsJsonPrimitive(s.getName()).getAsInt());
            else if (setting instanceof KeybindSetting s)
                s.setValue(settingsJson.getAsJsonPrimitive(s.getName()).getAsInt());
        }
    }
}
