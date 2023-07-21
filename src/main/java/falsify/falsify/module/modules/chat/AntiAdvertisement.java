package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.mixin.special.MixinMinecraft;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.Timer;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;

import java.util.UUID;

public class AntiAdvertisement extends ChatModule {

    private final BooleanSetting actionBar = new BooleanSetting("Action Bar", true);
    private final Timer timer = new Timer();
    public AntiAdvertisement() {
        super("AntiAd","Removes Minehut advertisements.", Category.MISC, -1);
        settings.add(actionBar);
    }

    @Override
    public void onChat(EventPacketRecieve eventPacketRecieve, String message) {
        if(message == null || message.split(" ").length == 0) return;
        if(message.split(" ")[0].toUpperCase().contains("[AD]")) {
            if(actionBar.getValue()) mc.player.sendMessage(Text.of(message), true);
            eventPacketRecieve.setCancelled(true);
        }
    }

    @Override
    public void onEvent(Event<?> event) {
        super.onEvent(event);
        if(event instanceof EventPacketRecieve e && e.getPacket() instanceof BossBarS2CPacket packet) {
            packet.accept(new BossBarS2CPacket.Consumer() {
                @Override
                public void add(UUID uuid, Text name, float percent, BossBar.Color color, BossBar.Style style, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
                    BossBarS2CPacket.Consumer.super.add(uuid, name, percent, color, style, darkenSky, dragonMusic, thickenFog);
                }

                @Override
                public void remove(UUID uuid) {
                    BossBarS2CPacket.Consumer.super.remove(uuid);
                }

                @Override
                public void updateProgress(UUID uuid, float percent) {
                    BossBarS2CPacket.Consumer.super.updateProgress(uuid, percent);
                }

                @Override
                public void updateName(UUID uuid, Text name) {
                    if(mc.player != null && ChatModuleUtils.concatArray(name.withoutStyle(), "").startsWith("Sending you to ") && timer.hasTimeElapsed(100, true)) {
                        mc.player.getInventory().selectedSlot = 2;
                        ((MixinMinecraft)mc).rightClick();
                    }
                    BossBarS2CPacket.Consumer.super.updateName(uuid, name);
                }

                @Override
                public void updateStyle(UUID id, BossBar.Color color, BossBar.Style style) {
                    BossBarS2CPacket.Consumer.super.updateStyle(id, color, style);
                }

                @Override
                public void updateProperties(UUID uuid, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
                    BossBarS2CPacket.Consumer.super.updateProperties(uuid, darkenSky, dragonMusic, thickenFog);
                }
            });
        }
    }
}
