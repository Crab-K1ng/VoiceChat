package io.github.CrabK1ng.Proximity.networking;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class AudioClientSender {

    private final Bootstrap bootstrap;
    private final Channel channel;
    private final InetSocketAddress serverAddress;

    public AudioClientSender(String serverHost, int port) throws Exception {
        this.serverAddress = new InetSocketAddress(serverHost, port);

        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                        // Handle incoming audio data here
                        ByteBuf content = packet.content();
                        byte[] audio = new byte[content.readableBytes()];
                        content.readBytes(audio);

                        // Play audio (left as exercise)
                    }
                });

        channel = bootstrap.bind(0).sync().channel();
    }

    public void sendAudio(String username, float x, float y, float z, byte[] audio) {
        ByteBuf buf = Unpooled.buffer();

        byte[] nameBytes = username.getBytes(StandardCharsets.UTF_8);
        buf.writeByte(nameBytes.length);
        buf.writeBytes(nameBytes);

        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);

        buf.writeBytes(audio);

//        Constants.LOGGER.info(username);
//        Constants.LOGGER.info(x);
//        Constants.LOGGER.info(y);
//        Constants.LOGGER.info(z);
//        Constants.LOGGER.info(audio);

        channel.writeAndFlush(new DatagramPacket(buf, serverAddress));
    }
}

