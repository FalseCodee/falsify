package falsify.falsify.utils.netty;

import falsify.falsify.Falsify;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ClientPacketListener;

public class NettyClientHandler extends SimpleChannelInboundHandler<Packet<ClientPacketListener>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<ClientPacketListener> packet) {
        packet.apply(Falsify.client.getListener());
    }
}
