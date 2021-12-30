package falsify.falsify.module;

import falsify.falsify.module.modules.*;
import falsify.falsify.module.modules.chat.TPAnnoy;
import falsify.falsify.module.modules.chat.rps;

import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<>();

    public static void init(){
        modules.add(new DiamondGrabber());
        modules.add(new GuiArrayList());
        modules.add(new Sentry());
        modules.add(new RunFromSpawn());
        modules.add(new AutoRespawn());
        modules.add(new Echo());
        modules.add(new rps());
        modules.add(new AutoFish());
        modules.add(new BungeeHack());
        modules.add(new TPAnnoy());

    }

    public static Module getModule(Module module){
        for(Module m : modules){
            if(m.name.equalsIgnoreCase(module.name)){
                return m;
            }
        }
        return null;
    }
}
