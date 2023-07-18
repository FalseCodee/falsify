package falsify.falsify.utils.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.primatives.Tab;
import falsify.falsify.module.Category;
import falsify.falsify.utils.config.translators.CategoryTranslator;
import falsify.falsify.utils.config.translators.ClickGuiTranslator;

import java.io.*;
import java.util.List;

public class ConfigManager {
    public JsonObject config;
    public final File configFile;

    public ConfigManager() {
        configFile = new File(Falsify.clientDir.getAbsolutePath() + "\\config.json");
        if(!configFile.exists()){
            config = new JsonObject();
        } else{
            try {
                config = readConfigFile();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadModules() {
        if(config.has("modules")) {
            JsonObject modules = config.getAsJsonObject("modules");
            for(Category category : Category.values()) {
                CategoryTranslator.loadCategory(category, modules.getAsJsonObject(category.getName()));
            }
        }
    }

    public void saveModules() {
        JsonObject modules = new JsonObject();
        for(Category category : Category.values()) {
            modules.add(category.getName(), CategoryTranslator.translateCategory(category));
        }
        config.add("modules", modules);
    }

    public void loadClickGui(List<Tab> tabs) {
        if(config.has("clickgui"))ClickGuiTranslator.loadClickGui(tabs, config.getAsJsonObject("clickgui"));
    }

    public void saveClickGui(List<Tab> tabs) {
        config.add("clickgui", ClickGuiTranslator.translateClickGui(tabs));
    }

    private JsonObject readConfigFile() throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(configFile));
        return jsonElement.getAsJsonObject();
    }

    public void saveConfigFile() {
        try(FileWriter writer = new FileWriter(configFile)) {
            writer.write(config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
