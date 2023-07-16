package falsify.falsify.module;

import falsify.falsify.Falsify;
import falsify.falsify.module.modules.chat.*;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.modules.combat.AutoClick;
import falsify.falsify.module.modules.misc.*;
import falsify.falsify.module.modules.movement.*;
import falsify.falsify.module.modules.player.AutoFish;
import falsify.falsify.module.modules.misc.AutoRespawn;
import falsify.falsify.module.modules.player.FastPlace;
import falsify.falsify.module.modules.render.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<>();

    public static void init(){
        modules.add(new GuiArrayList());
        modules.add(new Sentry());
        modules.add(new AutoRespawn());
        modules.add(new AutoFish());
        modules.add(new BungeeHack());
        modules.add(new TPAnnoy());
        modules.add(new AntiAdvertisement());
        modules.add(new VClip());
        modules.add(new ClientBrand());
        modules.add(new Aimbot());
        modules.add(new AutoWalk());
        modules.add(new AutoJump());
        modules.add(new Sprint());
        modules.add(new FastPlace());
        modules.add(new AutoClick());
        modules.add(new AutoMath());
        modules.add(new Trajectories());
        modules.add(new Flight());
        modules.add(new SafeFarm());
        modules.add(new BoatFly());
        modules.add(new ESP());
        modules.add(new DupeDrop());
        modules.add(new BodyGuard());
        modules.add(new ChatBot());
        modules.add(new ElytraFly());
        modules.add(new NoFall());
        modules.add(new EntityInfo());
        modules.add(new FaceCover());
        modules.add(new Fullbright());
        modules.add(new Chams());
        modules.add(new BlurModule());
        modules.add(new ProtocolChanger());
        modules.add(new Zoom());

        modules.add(new FPSModule());
        modules.add(new TimeModule());
        modules.add(new ServerAddressModule());
        modules.add(new ServerPingModule());
        modules.add(new PlayersOnlineModule());
        modules.add(new BiomeModule());
        modules.add(new LightLevelModule());
        modules.add(new CPSModule());
        modules.add(new ArmorModule());
        modules.add(new TitleModule());
        modules.add(new WASDModule());
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
