package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketSend;

import falsify.falsify.mixin.special.MixinHandshakeC2SPacket;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.lwjgl.glfw.GLFW;

public class BungeeHack extends Module {
    MinecraftClient mc = MinecraftClient.getInstance();
    public String ip = "1.2.3.4";
    public String uuid = mc.getSession().getUuid().replace("-","");

    public BungeeHack() {
        super("BungeeHack", Category.MISC, GLFW.GLFW_KEY_COMMA);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventPacketSend){
            if(((EventPacketSend) e).getPacket() instanceof HandshakeC2SPacket){
                HandshakeC2SPacket p = (HandshakeC2SPacket) ((EventPacketSend) e).packet;
                if(p.getIntendedState().equals(NetworkState.LOGIN)){
                    ((MixinHandshakeC2SPacket)p).setAddress(((MixinHandshakeC2SPacket)p).getAddress() + "\000"+ip+"\000"+uuid);
                }
            }
        }
    }
}
