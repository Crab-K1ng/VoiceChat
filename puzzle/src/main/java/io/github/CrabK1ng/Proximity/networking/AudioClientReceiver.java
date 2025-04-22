package io.github.CrabK1ng.Proximity.networking;

import io.github.CrabK1ng.Proximity.Utils.Utils;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import javax.sound.sampled.*;

public class AudioClientReceiver {

    public void start(int listenPort) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    SourceDataLine speakers;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
                        try {
                            AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, false);
                            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                            speakers = (SourceDataLine) AudioSystem.getLine(info);
                            speakers.open(format);
                            speakers.start();
                        } catch (LineUnavailableException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                        ByteBuf content = packet.content();
                        byte[] audio = new byte[content.readableBytes()];
                        content.readBytes(audio);

                        OpusDecoderHandler decoder;
                        try {
                            decoder = new OpusDecoderHandler(8000, 1);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        short[] decoded;
                        try {
                            decoded = decoder.decode(audio);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to decode Opus data", e);
                        }

//                byte[] byteDecoded = Proximity.shortToByteArray(decoded);
                        //////////////////////////////////////////////////////////
                        byte[] byteDecoded = Utils.shortsToBytes(decoded);

                        // Write to audio output
                        if (speakers != null) {
                            speakers.write(byteDecoded, 0, byteDecoded.length);
                        }
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        cause.printStackTrace();
                        ctx.close();
                    }
                });

        Channel channel = bootstrap.bind(listenPort).sync().channel();
        channel.closeFuture().await();
    }
}

