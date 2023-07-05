package falsify.falsify.module;

import falsify.falsify.listeners.Event;
import falsify.falsify.module.settings.KeybindSetting;
import falsify.falsify.module.settings.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public final String name;
    public final String description;
    public boolean toggled;
    public final KeybindSetting keybind;
    public final Category category;
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category, int keyCode){
        this.name = name;
        this.description = description;
        this.category = category;
        keybind = new KeybindSetting("Keybind", keyCode);
        settings.add(keybind);
    }
    public Module(String name, String description, Category category, int keyCode, boolean enabled){
       this(name, description, category, keyCode);
        if(enabled){
            this.toggle();
        }
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public int getKeyCode() {
        return keybind.getValue();
    }

    public void toggle(){
        this.toggled = !this.toggled;
        if(toggled){
            ModuleManager.enabledModules.add(this);
            onEnable();
        } else{
            ModuleManager.enabledModules.remove(this);
            onDisable();
        }
    }
    public void onEnable(){

    }
    public void onDisable(){

    }
    public void onEvent(Event<?> event){

    }
}
