package falsify.falsify.module.modules;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventTrack;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Sentry extends Module {
    public Sentry() {
        super("Sentry", Category.MISC, GLFW.GLFW_KEY_B);
    }
    public ArrayList<Entity> rendered;
    private final DecimalFormat format = new DecimalFormat("#.#");

    @Override
    public void onEnable() {
        rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity).sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName()))).collect(Collectors.toList()));
        rendered.forEach(entity -> entity.setGlowing(true));
    }

    @Override
    public void onDisable() {
        rendered.forEach(entity -> entity.setGlowing(false));
    }

    final int max = mc.textRenderer.getWidth("WWWWWWWWWWWWWWWW WWWWW");

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventTrack){
            rendered.forEach(entity -> entity.setGlowing(false));
            rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity).sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName()))).collect(Collectors.toList()));
            rendered.forEach(entity -> entity.setGlowing(true));
        }
        if(e instanceof EventRender){
            if(rendered != null && rendered.size() > 0){
                AtomicInteger count = new AtomicInteger();
                AtomicInteger count2 = new AtomicInteger();

                rendered.stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
                        .forEach(entity -> {
                            if (mc.getWindow().getScaledHeight() - ((mc.textRenderer.fontHeight) * count.get()) - mc.textRenderer.fontHeight - 15 <= mc.getWindow().getScaledHeight() / 2 + 50) {
                                count2.getAndIncrement();
                                count.set(0);
                            }
                            RenderUtils.AlignText("(" + format.format(entity.distanceTo(mc.player)) + "m) " + ((PlayerEntity) entity).getGameProfile().getName(),
                                    max * count2.get() + 15,
                                    mc.getWindow().getScaledHeight() - ((mc.textRenderer.fontHeight) * count.get()) - mc.textRenderer.fontHeight - 15,
                                    RenderUtils.getIntFromColor(Math.round(Math.max(0, 255-(entity.distanceTo(mc.player)*entity.distanceTo(mc.player)))),Math.round(Math.min(255, entity.distanceTo(mc.player)*entity.distanceTo(mc.player))),0),
                                    Alignment.RIGHT);
                            count.getAndIncrement();
                        });
            }
        }
    }

}

