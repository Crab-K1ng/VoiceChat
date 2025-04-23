package io.github.CrabK1ng.Proximity.networking;

import io.github.CrabK1ng.Proximity.networking.packets.ProxPacket;
import io.github.CrabK1ng.Proximity.serialization.IKeylessSerializer;
import io.github.CrabK1ng.Proximity.serialization.KeylessBinarySerializer;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProxNetIdentity {

    ChannelHandlerContext context;

    public ProxNetIdentity(ChannelHandlerContext context) {
        this.context = context;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void send(ProxPacket packet) throws IOException {
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
