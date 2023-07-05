package falsify.falsify.utils.config.translators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;

import java.util.List;

public class CategoryTranslator {
    public static JsonObject translateCategory(Category category) {
        JsonObject json = new JsonObject();
        List<Module> modules = ModuleManager.modules.stream().filter(m -> m.category == category).toList();
        for(Module module : modules) {
            json.add(module.name, ModuleTranslator.translateModule(module));
        }
        return json;
    }

    public static void loadCategory(Category category, JsonObject categoryJson) {
        List<Module> modules = ModuleManager.modules.stream().filter(m -> m.category == category).toList();
        for(Module module : modules) {
            if(categoryJson.has(module.name))
                ModuleTranslator.loadModule(module, categoryJson.getAsJsonObject(module.name));
        }
    }
}
