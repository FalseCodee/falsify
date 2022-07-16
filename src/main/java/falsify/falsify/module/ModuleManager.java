package falsify.falsify.module;

import falsify.falsify.module.modules.*;
import falsify.falsify.module.modules.chat.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<>();

    public static void init(){
        modules.add(new DiamondGrabber());
        modules.add(new GuiArrayList());
        modules.add(new Sentry());
        modules.add(new RunFromSpawn());
        modules.add(new AutoRespawn());
        modules.add(new AutoFish());
        modules.add(new BungeeHack());
        modules.add(new TPAnnoy());
        modules.add(new AntiAdvertisement());
        modules.add(new TextBrush());
        modules.add(new VClip());
        modules.add(new ClientBrand());
        modules.add(new Aimbot());
        modules.add(new AutoWalk());
        modules.add(new AutoJump());
        modules.add(new Sprint());
        modules.add(new FastPlace());
        modules.add(new Spectate());
        modules.add(new AutoClick());
        modules.add(new AutoMath());

    }

    public static <T extends Module> T getModule(Class<T> module){
        for(Module m : modules){
            if(m.getClass().equals(module)){
                try {
                    return module.cast(m);
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
