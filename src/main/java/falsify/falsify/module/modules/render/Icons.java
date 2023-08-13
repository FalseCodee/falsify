package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import me.falsecode.netty.packet.packets.c2s.ClientQueryRequestPlayerListPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;

import java.awt.*;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class Icons extends Module {
    public static final HashSet<UUID> users = new HashSet<>();
    public Icons() {
        super("Icons", "Shows icons above people using Legacy", false, Category.RENDER, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventPacketRecieve e) {
            if(e.getPacket() instanceof PlayerListS2CPacket packet){
                Falsify.client.send(new ClientQueryRequestPlayerListPacket(packet.getEntries().stream().map(PlayerListS2CPacket.Entry::profileId).collect(Collectors.toSet())));
            } else if (e.getPacket() instanceof PlayerRemoveS2CPacket packet) {
                packet.profileIds().forEach(users::remove);
            }
        }
    }

    public static void render(DrawContext context) {
        context.getMatrices().translate(9, -2.5, 0);
        Falsify.fontRenderer.drawString(context, "[L]", 0, 0, new Color(255, 53, 53), true);
    }
}
