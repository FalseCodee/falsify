package falsify.falsify.utils.config.translators;

import com.google.gson.JsonObject;
import falsify.falsify.automation.block.AutomationBlock;
import falsify.falsify.automation.block.IfBlock;
import falsify.falsify.automation.block.SendChatMessageBlock;
import falsify.falsify.automation.block.WaitBlock;
import falsify.falsify.module.DisplayModule;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.*;

import java.util.ArrayList;
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
        for (Setting<?> setting : settings) {
            if (setting instanceof BooleanSetting s) json.addProperty(s.getName(), s.getValue());
            else if (setting instanceof RangeSetting s) json.addProperty(s.getName(), s.getValue());
            else if (setting instanceof ModeSetting s) json.addProperty(s.getName(), s.getIndex());
            else if (setting instanceof KeybindSetting s) json.addProperty(s.getName(), s.getValue());
            else if (setting instanceof ColorSetting s) {
                JsonObject colorObj = new JsonObject();
                colorObj.addProperty("hue", s.getHue());
                colorObj.addProperty("saturation", s.getSaturation());
                colorObj.addProperty("brightness", s.getBrightness());
                colorObj.addProperty("alpha", s.getAlpha());
                colorObj.addProperty("rainbow", s.isRainbow());
                colorObj.addProperty("rainbowSpeed", s.getRpm());
                json.add(s.getName(), colorObj);
            }
            else if(setting instanceof AutomationBlockSetting s) {
                json.add(s.getName(), translateBlocks(s.getValue()));
            }
            else if (setting instanceof TextSetting s) json.addProperty(s.getName(), s.getValue());
            else if (setting instanceof FreeNumberSetting s) json.addProperty(s.getName(), s.getValue());

        }
        return json;
    }

    private static JsonObject translateBlocks(ArrayList<AutomationBlock> blocks) {
        JsonObject blockList = new JsonObject();
        for(int i = 0; i < blocks.size(); i++) {
            AutomationBlock block = blocks.get(i);
            blockList.add("b" + i, translateBlock(block));
        }
        return blockList;
    }
    private static JsonObject translateBlock(AutomationBlock block) {
        JsonObject blockObj = new JsonObject();
        blockObj.addProperty("type", block.getName());
        switch (block.getName()) {
            case "Wait" -> blockObj.addProperty("delay", ((WaitBlock) block).getDelay());
            case "Send Message" -> blockObj.addProperty("message", ((SendChatMessageBlock) block).getMessage());
        }
        if(block instanceof IfBlock ifBlock) blockObj.add("blocks", translateBlocks(ifBlock.getBlocks()));
        return blockObj;
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
            else if (setting instanceof ColorSetting s) {
                JsonObject colorObj = settingsJson.getAsJsonObject(s.getName());
                s.setHue(colorObj.getAsJsonPrimitive("hue").getAsFloat());
                s.setSaturation(colorObj.getAsJsonPrimitive("saturation").getAsFloat());
                s.setBrightness(colorObj.getAsJsonPrimitive("brightness").getAsFloat());
                s.setAlpha(colorObj.getAsJsonPrimitive("alpha").getAsFloat());
                s.setRainbow(colorObj.getAsJsonPrimitive("rainbow").getAsBoolean());
                s.setRpm(colorObj.getAsJsonPrimitive("rainbowSpeed").getAsFloat());
            } else if (setting instanceof AutomationBlockSetting s) {
                loadBlocks(settingsJson.getAsJsonObject(s.getName())).forEach(s::addBlock);
            } else if (setting instanceof TextSetting s) {
                s.setValue(settingsJson.getAsJsonPrimitive(s.getName()).getAsString());
            } else if (setting instanceof FreeNumberSetting s) {
                s.setValue(settingsJson.getAsJsonPrimitive(s.getName()).getAsDouble());
            }
        }
    }

    private static ArrayList<AutomationBlock> loadBlocks(JsonObject blockList) {
        ArrayList<AutomationBlock> blocks = new ArrayList<>();
        for(String obj : blockList.keySet()) {
            JsonObject blockObj = blockList.getAsJsonObject(obj);
            AutomationBlock block = null;

            switch (blockObj.getAsJsonPrimitive("type").getAsString()) {
                case "If" -> block = new IfBlock(obj1 -> true);
                case "Wait" -> block = new WaitBlock(blockObj.getAsJsonPrimitive("delay").getAsInt());
                case "Send Message" -> block = new SendChatMessageBlock(blockObj.getAsJsonPrimitive("message").getAsString());
            }
            if (block == null) continue;

            if(block instanceof IfBlock ifBlock) loadBlocks(blockObj.getAsJsonObject("blocks")).forEach(ifBlock::addBlock);

            blocks.add(block);
        }
        return blocks;
    }
}
