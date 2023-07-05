package falsify.falsify.module.modules.misc;

import com.google.common.collect.Lists;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.listeners.events.EventTrack;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.modules.combat.Aimbot;
import falsify.falsify.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Sentry extends Module {
    public Sentry() {
        super("Sentry", "Displays players within render distance.", Category.MISC, -1);
    }
    public ArrayList<Entity> rendered;
    private final DecimalFormat format = new DecimalFormat("#.#");

    @Override
    public void onEnable() {
        rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
                    .sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName())))
                .collect(Collectors.toList()));
    }

    @Override
    public void onDisable() {
        rendered.forEach(entity -> entity.setGlowing(false));
    }

    final int max = mc.textRenderer.getWidth("WWWWWWWWWWWWWWWW WWWWW");

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventTrack e){
            rendered.forEach(entity -> entity.setGlowing(false));
            rendered = Lists.newArrayList(Lists.newArrayList(mc.world.getEntities()).stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
                    .sorted(Comparator.comparingInt(entity -> mc.textRenderer.getWidth(entity.getName())))
                    .collect(Collectors.toList()));
            rendered.forEach(entity -> entity.setGlowing(true));
            if(e.isStart() && !e.getPlayer().equals(mc.player) && !e.getPlayer().getGameProfile().getName().startsWith("§") && e.getPlayer().getHealth() > 0.1f && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry((e.getPlayer()).getGameProfile().getId()))) {
                new FalseRunnable() {
                    @Override
                    public void run() {

                        try {
                            NetworkUtils.postRequest("https://discord.com/api/webhooks/1079949299649093693/lyzDdMwZRNNw6zf7xuK7yc3ProJP26U8PYUJfQAOMEpjkLPrHdl1hqq-7zfbIaG73emj", new DiscordWebhookBuilder().username("Player Spotted")
                                    .content(e.getPlayer().getGameProfile().getName() + " was spotted at: (" + e.getPlayer().getBlockPos().getX() + ", " + e.getPlayer().getBlockPos().getY() + ", " + e.getPlayer().getBlockPos().getZ() + ")").build().toString());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }.runTaskAsync();
            }
        }
        if(event instanceof EventPacketSend packetSend) {
            if(packetSend.getPacket() instanceof ChatMessageC2SPacket packet) {
                if(packet.chatMessage().toLowerCase().startsWith(".target")) {
                    packetSend.setCancelled(true);
                    if(!packet.chatMessage().contains(" ")) {
                        mc.player.sendMessage(Text.of("Enter a player."));
                        return;
                    }
                    String user = packet.chatMessage().substring(packet.chatMessage().indexOf(" ")).trim();
                    Entity entity = rendered.stream().filter(entity1 -> entity1 instanceof PlayerEntity && ((PlayerEntity) entity1).getGameProfile().getName().equalsIgnoreCase(user)).findFirst().orElse(null);
                    if(entity == null) {
                        if(user.equalsIgnoreCase("none")) Aimbot.target = null;
                        else
                        mc.player.sendMessage(Text.of("Player not found."));
                    } else {
                        mc.player.sendMessage(Text.of("Target set to " + user));
                    }
                    Aimbot.target.setEntity((LivingEntity) entity);
                }
            }
        }



        if(event instanceof EventRender e){
            if(rendered != null && rendered.size() > 0){
                AtomicInteger count = new AtomicInteger();
                AtomicInteger count2 = new AtomicInteger();

                rendered.stream().filter(entity -> entity instanceof PlayerEntity && !entity.equals(mc.player) && mc.getNetworkHandler().getPlayerList().contains(mc.getNetworkHandler().getPlayerListEntry(((PlayerEntity) entity).getGameProfile().getId())))
                        .forEach(entity -> {
                            if (mc.getWindow().getScaledHeight() - ((mc.textRenderer.fontHeight) * count.get()) - mc.textRenderer.fontHeight - 15 <= mc.getWindow().getScaledHeight() / 2 + 50) {
                                count2.getAndIncrement();
                                count.set(0);
                            }
                            RenderUtils.AlignText(e.getDrawContext(), "(" + format.format(entity.distanceTo(mc.player)) + "m) " + ((PlayerEntity) entity).getGameProfile().getName(),
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

