package falsify.falsify.utils.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import falsify.falsify.Falsify;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.module.Category;
import falsify.falsify.utils.config.translators.CategoryTranslator;
import falsify.falsify.utils.config.translators.ModMenuTranslator;
import falsify.falsify.utils.config.translators.WaypointTranslator;

import java.io.*;

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
        Falsify.logger.info("Created: Config Manager");
    }

    public void loadModules() {
        if(config.has("modules")) {
            JsonObject modules = config.getAsJsonObject("modules");
            for(Category category : Category.values()) {
                if(modules.has(category.getName()))
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

    public void saveWaypoints() {
        config.add("waypoints", WaypointTranslator.translateWaypoints(Falsify.waypointManager.getWaypoints()));
    }

    public void loadWaypoints() {
        if(config.has("waypoints")) {
            WaypointTranslator.loadWaypoints(config.getAsJsonArray("waypoints"));
        }
    }

    public void loadPanel(Panel panel) {
        if(config.has("clickgui")) ModMenuTranslator.loadPanel(panel, config.getAsJsonObject("clickgui"));
    }

    public void saveClickGui(Panel panel) {
        config.add("clickgui", ModMenuTranslator.translatePanel(panel));
    }

    private JsonObject readConfigFile() throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(configFile));
        Falsify.logger.info("Loaded Config");
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
