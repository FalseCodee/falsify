package falsify.falsify.module;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventKey;
import falsify.falsify.module.settings.Setting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public String name;
    public boolean toggled;
    public int keyCode;
    public Category category;
    public static MinecraftClient mc = MinecraftClient.getInstance();
    public List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, Category category, int keyCode){
        this.name = name;
        this.category = category;
        this.keyCode = keyCode;
    }
    public Module(String name, Category category, int keyCode, boolean enabled){
        this.name = name;
        this.category = category;
        this.keyCode = keyCode;
        if(enabled){
            this.toggle();
        }
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public int getKeyCode() {
        return keyCode;
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
    public void onEvent(Event<?> e){

    }
}
