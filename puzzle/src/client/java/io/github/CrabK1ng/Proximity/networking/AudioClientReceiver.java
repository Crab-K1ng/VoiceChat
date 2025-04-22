package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.math.Vector3;
import io.github.CrabK1ng.Proximity.AudioSetting;
import io.github.CrabK1ng.Proximity.Proximity;
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

public class AudioClientReceiver {

    public void start(int listenPort) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
//                    SourceDataLine speakers;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
//                        try {
//                            DataLine.Info info = new DataLine.Info(SourceDataLine.class, AudioSetting.getFormat());
//                            speakers = (SourceDataLine) AudioSystem.getLine(info);
//                            speakers.open(AudioSetting.getFormat());
//                            speakers.start();
//                        } catch (LineUnavailableException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                        ByteBuf content = packet.content();

                        int nameLength = content.readByte();
                        content.readBytes(nameLength);

                        Vector3 vector3 = new Vector3();
                        vector3.x = content.readFloat();
                        vector3.y =  content.readFloat();
                        vector3.z =  content.readFloat();

                        byte[] audio = new byte[content.readableBytes()];
                        content.readBytes(audio); // Moves the reader index forward



                        OpusDecoderHandler decoder;
                        try {
                            decoder = new OpusDecoderHandler(AudioSetting.getSampleRate(), AudioSetting.getChannels());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        short[] decoded;
                        try {
                            decoded = decoder.decode(audio);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to decode Opus data", e);
                        }

                        byte[] byteDecoded = Utils.shortsToBytes(decoded);

                        // Write to audio output
                        if (Proximity.speakers != null) {
                            Proximity.speakers.write(byteDecoded, 0, audio.length);
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

