package io.github.CrabK1ng.Proximity;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioRelayServer {

    private final int port;
    private final List<InetSocketAddress> clients = Collections.synchronizedList(new ArrayList<>());

    public AudioRelayServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        Constants.LOGGER.info("Initialized relay server");

        Bootstrap b = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                            ByteBuf buf = packet.content();
                            InetSocketAddress sender = packet.sender();

                            // Add sender to client list if not present
                            if (clients.stream().noneMatch(c -> c.equals(sender))) {
                                clients.add(sender);
                            }

                            // Relay audio to all except sender
                            for (InetSocketAddress client : clients) {
                                if (!client.equals(sender)) {
                                    ctx.writeAndFlush(new DatagramPacket(buf, client));
                                }
                            }
                        }
                    });

            b.bind(port).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}
