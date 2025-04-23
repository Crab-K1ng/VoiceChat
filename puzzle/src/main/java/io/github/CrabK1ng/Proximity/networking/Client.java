package io.github.CrabK1ng.Proximity.networking;

import io.github.CrabK1ng.Proximity.networking.packets.ProxPacket;
import io.github.CrabK1ng.Proximity.serialization.IKeylessSerializer;
import io.github.CrabK1ng.Proximity.serialization.KeylessBinarySerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Client {

    static EventLoopGroup clientGroup;
    public static Channel context;

    public static void connect(String host, int port) throws InterruptedException {
        clientGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ProxChannelInitializer());

        ChannelFuture future = bootstrap.connect(host, port).sync();
        Client.context = future.channel();
    }

    public static void shutdown() {
        clientGroup.shutdownGracefully();
    }

    public static void send(ProxPacket packet) throws IOException {
        IKeylessSerializer serializer = new KeylessBinarySerializer();
        packet.write(serializer);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bytes = serializer.toCompressedBytes();

        short id = ProxPacket.REVERSE_PACKET_MAP.get(packet.getClass());
        stream.write((byte) (id >>> 8));
        stream.write((byte) (id));
        stream.write(bytes, 0, bytes.length);

        context.writeAndFlush(stream.toByteArray());
    }

}
