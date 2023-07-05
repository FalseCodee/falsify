package falsify.falsify.utils.config.translators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;

import java.util.List;

public class CategoryTranslator {
    public static JsonArray translateCategory(Category category) {
        JsonArray json = new JsonArray();
        List<Module> modules = ModuleManager.modules.stream().filter(m -> m.category == category).toList();
        for(Module module : modules) {
            json.add(ModuleTranslator.translateModule(module));
        }
        return json;
    }

    public static void loadCategory(Category category, JsonArray categoryJson) {
        List<Module> modules = ModuleManager.modules.stream().filter(m -> m.category == category).toList();
        for(int i = 0; i < categoryJson.size(); i++) {
            ModuleTranslator.loadModule(modules.get(i), categoryJson.get(i).getAsJsonObject());
        }
    }
}
