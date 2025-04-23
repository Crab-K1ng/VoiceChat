package io.github.CrabK1ng.Proximity.networking;

import io.github.CrabK1ng.Proximity.utils.FixedArray;
import io.github.CrabK1ng.Proximity.networking.packets.ProxPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Server {

    static EventLoopGroup mainGroup;
    static ChannelGroup channelGroup;

    public static FixedArray<ProxNetIdentity> identities = new FixedArray<>();
    public static Map<ChannelHandlerContext, ProxNetIdentity> identityMap = new HashMap<>();

    public static void start(int port) throws InterruptedException {
        Server.identities.clear();
        Server.identityMap.clear();

        mainGroup = new NioEventLoopGroup();
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.AUTO_CLOSE, true)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ProxChannelInitializer());

            ChannelFuture future = bootstrap.bind(port).sync();
            channelGroup.add(future.channel());
        } catch (Exception e) {
            shutdown();
        }
    }

    public static void shutdown() {
        channelGroup.clear();
        mainGroup.shutdownGracefully();
    }

    public static void send(ProxPacket packet) throws IOException {
        for (ProxNetIdentity identity : Server.identities) {
            identity.send(packet);
        }
    }
}
