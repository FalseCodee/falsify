package falsify.falsify.utils.packet;

import falsify.falsify.Falsify;
import falsify.falsify.module.modules.render.Icons;
import falsify.falsify.module.modules.render.Notifications;
import me.falsecode.netty.packet.listeners.ClientPacketListener;
import me.falsecode.netty.packet.packets.s2c.PlayerMessagePacket;
import me.falsecode.netty.packet.packets.s2c.ServerNotificationPacket;
import me.falsecode.netty.packet.packets.s2c.ServerQueryResponsePlayerListPacket;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class DefaultClientPacketListener implements ClientPacketListener {
    @Override
    public void onServerNotification(ServerNotificationPacket packet) {
        Notifications.addNotification(packet.getContent());
    }

    @Override
    public void onPlayerMessage(PlayerMessagePacket packet) {
        if(Falsify.mc.player == null) return;

        Falsify.mc.player.sendMessage(Text.of("§7[DM From §f"+ packet.getUser() +"§7] §f" + packet.getContent()));
    }

    @Override
    public void onServerQueryResponsePlayerList(ServerQueryResponsePlayerListPacket packet) {
        List<UUID> toAdd = packet.getUuids().stream().filter(uuid -> !Icons.users.contains(uuid)).toList();
        Icons.users.addAll(toAdd);
    }
}
