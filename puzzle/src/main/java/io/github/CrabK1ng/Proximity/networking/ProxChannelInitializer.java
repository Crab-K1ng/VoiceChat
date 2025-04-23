package io.github.CrabK1ng.Proximity.networking;

import io.github.CrabK1ng.Proximity.networking.encoding.ByteArrayDataDecoder;
import io.github.CrabK1ng.Proximity.networking.encoding.ByteArrayDataEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ProxChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new ByteArrayDataEncoder());
        pipeline.addLast(new ByteArrayDataDecoder());
        pipeline.addLast(new PacketHandler());
    }

}
