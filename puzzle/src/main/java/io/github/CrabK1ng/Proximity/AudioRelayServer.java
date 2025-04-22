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
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                            ByteBuf buf = packet.content();
                            InetSocketAddress sender = packet.sender();

                            // Parse the username and position
                            int nameLen = buf.readUnsignedByte();
                            buf.skipBytes(nameLen); // Skip username
                            buf.skipBytes(12); // Skip 3 floats for position

                            // Get remaining bytes (audio)
                            byte[] audio = new byte[buf.readableBytes()];
                            buf.readBytes(audio);

                            Constants.LOGGER.info(audio);

                            // Add sender to client list if not present
                            if (clients.stream().noneMatch(c -> c.equals(sender))) {
                                clients.add(sender);
                            }

                            // Relay audio to all except sender
                            for (InetSocketAddress client : clients) {
                                if (!client.equals(sender)) {
                                    ByteBuf outBuf = Unpooled.wrappedBuffer(audio);
                                    ctx.writeAndFlush(new DatagramPacket(outBuf, client));
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
