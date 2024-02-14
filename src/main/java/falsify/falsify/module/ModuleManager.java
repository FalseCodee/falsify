package falsify.falsify.module;

import falsify.falsify.Falsify;
import falsify.falsify.module.modules.chat.*;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.module.modules.combat.AutoClick;
import falsify.falsify.module.modules.misc.*;
import falsify.falsify.module.modules.movement.*;
import falsify.falsify.module.modules.player.*;
import falsify.falsify.module.modules.render.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<>();

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
        modules.add(new Quote());
        modules.add(new SafeWalk());
        modules.add(new Perspective());
        modules.add(new FastJump());
        modules.add(new Breadcrumbs());
        modules.add(new ServerCrasher());
        modules.add(new Notifications());
        modules.add(new PrivateChat());
        modules.add(new Icons());
        modules.add(new AutoHeal());
        modules.add(new AutoMine());
        modules.add(new AutoPlace());
        modules.add(new Freecam());
        modules.add(new BaseFinder());
        modules.add(new AutoTool());
        modules.add(new AutoEat());
        modules.add(new PluginFinder());
        modules.add(new FindUnicode());
        modules.add(new AutoSell());
        modules.add(new Nametags());
        modules.add(new TazCrafterDefamation());

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
        modules.add(new VelocityModule());
        modules.add(new PositionModule());
        modules.add(new GifModule());
        modules.add(new PotionEffectModule());
        modules.add(new ScoreboardModule());

//        Falsify.postProcess = new PostProcess();
//        modules.add(Falsify.postProcess);
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

    public static void disableCheats() {
        modules = new CopyOnWriteArrayList<>(modules.stream().filter(module -> !module.isCheat).toList());
        enabledModules = new CopyOnWriteArrayList<>(enabledModules.stream().filter(module -> !module.isCheat).toList());
    }
}
