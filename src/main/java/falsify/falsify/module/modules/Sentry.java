package falsify.falsify.module.modules;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventTrack;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Sentry extends Module {
    public Sentry() {
        super("Sentry", Category.MISC, GLFW.GLFW_KEY_B);
    }
    public ArrayList<Entity> rendered;
    //private final DecimalFormat format = new DecimalFormat("#.#");

    @Override
    public void onEnable() {
        rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
//                    .sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName())))
                .collect(Collectors.toList()));
    }

    @Override
    public void onDisable() {
        rendered.forEach(entity -> entity.setGlowing(false));
    }

//    final int max = mc.textRenderer.getWidth("WWWWWWWWWWWWWWWW WWWWW");

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventTrack){
            rendered.forEach(entity -> entity.setGlowing(false));
            rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
//                    .sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName())))
                    .collect(Collectors.toList()));
            rendered.forEach(entity -> entity.setGlowing(true));
        }
        if(event instanceof EventPacketSend packetSend) {
            if(packetSend.getPacket() instanceof ChatMessageC2SPacket packet) {
                if(packet.getChatMessage().toLowerCase().startsWith(".target")) {
                    packetSend.setCancelled(true);
                    if(!packet.getChatMessage().contains(" ")) {
                        mc.player.sendMessage(Text.of("Enter a player."));
                        return;
                    }
                    String user = packet.getChatMessage().substring(packet.getChatMessage().indexOf(" ")).trim();
                    Entity entity = rendered.stream().filter(entity1 -> entity1 instanceof PlayerEntity && ((PlayerEntity) entity1).getGameProfile().getName().equalsIgnoreCase(user)).findFirst().orElse(null);
                    if(entity == null) {
                        mc.player.sendMessage(Text.of("Player not found."));
                    } else {
                        mc.player.sendMessage(Text.of("Target set to " + user));
                    }
                    Aimbot.target = entity;
                }
            }
        }



//        if(event instanceof EventRender){
//            if(rendered != null && rendered.size() > 0){
//                AtomicInteger count = new AtomicInteger();
//                AtomicInteger count2 = new AtomicInteger();
//
//                rendered.stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
//                        .forEach(entity -> {
//                            if (mc.getWindow().getScaledHeight() - ((mc.textRenderer.fontHeight) * count.get()) - mc.textRenderer.fontHeight - 15 <= mc.getWindow().getScaledHeight() / 2 + 50) {
//                                count2.getAndIncrement();
//                                count.set(0);
//                            }
//                            RenderUtils.AlignText("(" + format.format(entity.distanceTo(mc.player)) + "m) " + ((PlayerEntity) entity).getGameProfile().getName(),
//                                    max * count2.get() + 15,
//                                    mc.getWindow().getScaledHeight() - ((mc.textRenderer.fontHeight) * count.get()) - mc.textRenderer.fontHeight - 15,
//                                    RenderUtils.getIntFromColor(Math.round(Math.max(0, 255-(entity.distanceTo(mc.player)*entity.distanceTo(mc.player)))),Math.round(Math.min(255, entity.distanceTo(mc.player)*entity.distanceTo(mc.player))),0),
//                                    Alignment.RIGHT);
//                            count.getAndIncrement();
//                        });
//            }
//        }
    }

}

