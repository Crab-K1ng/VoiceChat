package io.github.CrabK1ng.Proximity.networking;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import io.github.CrabK1ng.Proximity.utils.BufferUtil;
import io.github.CrabK1ng.Proximity.networking.packets.ProxPacket;
import io.github.CrabK1ng.Proximity.serialization.KeylessBinaryDeserializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

public class PacketHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (Constants.SIDE != EnvType.SERVER) return;
        String address = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();

        System.out.println("Handler Joined " + address);

        ProxNetIdentity identity = new ProxNetIdentity(ctx);
        Server.identityMap.put(ctx, identity);
        Server.identities.add(identity);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (Constants.SIDE != EnvType.SERVER) return;
        String address = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        ProxNetIdentity identity = Server.identityMap.get(ctx);
        if (Server.identities.contains(identity, false))
            Server.identities.removeValue(identity, false);
        Server.identityMap.remove(ctx);

        System.out.println("Handler Left " + address);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = null;

        try {
            m = (ByteBuf)msg;

            short id = m.readShort();
            Class<? extends ProxPacket> packetClass = ProxPacket.PACKET_MAP.get((Short) id);
            System.out.println("Packet Received " + id);
            if (packetClass == null) return;

            ProxPacket packet = packetClass.newInstance();
            packet.read(KeylessBinaryDeserializer.fromBytes(BufferUtil.toByteArray(m), true));
            packet.handle(Constants.SIDE, Server.identityMap.get(ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
