package falsify.falsify.utils.netty;

import falsify.falsify.Falsify;
import falsify.falsify.utils.packet.DefaultClientPacketListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.falsecode.netty.packet.Packet;
import me.falsecode.netty.packet.listeners.ClientPacketListener;
import me.falsecode.netty.packet.packets.c2s.HandshakePacket;

public class NettyClient {

    private final String host;
    private final int port;
    private Channel channel;
    private EventLoopGroup group;
    private final ClientPacketListener listener;
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        listener = new DefaultClientPacketListener();
        Falsify.logger.info("Created: Netty Client");
    }

    public void run() {
        group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer());

            channel = bootstrap.connect(host, port).sync().channel();
            send(new HandshakePacket(Falsify.mc.player.getGameProfile().getId(), Falsify.mc.getSession().getUsername()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Falsify.logger.info("Netty Client Connected");
    }

    public void send(Packet<?> packet) {
        if(channel == null) return;
        channel.writeAndFlush(packet);
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public ClientPacketListener getListener() {
        return listener;
    }
}
