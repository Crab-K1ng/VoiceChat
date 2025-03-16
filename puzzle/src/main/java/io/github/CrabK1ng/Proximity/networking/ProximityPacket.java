package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.utils.ByteArray;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.github.CrabK1ng.Proximity.Proximity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ProximityPacket extends GamePacket {

    byte[] buffer;

    public ProximityPacket(){};

    public ProximityPacket(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public void receive(ByteBuf in) {
        if (in.hasArray()) {
            byte[] array = in.array();
            this.buffer = array;
        } else {
            byte[] array = new byte[in.readableBytes()];
            in.getBytes(in.readerIndex(), array);
            this.buffer = array;
        }
    }

    @Override
    public void write() {
        this.writeByteArray(ByteArray.with(this.buffer));
    }

    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (!identity.isServer()) {
            if (!(buffer == null)){
                Proximity.speakers.write(buffer, 0, buffer.length);
            }
        }
        if (identity.isServer()){
            if (!(buffer == null)){
                ProximityPacket ProximityPacket = new ProximityPacket(buffer.clone());
                ServerSingletons.SERVER.broadcastToAll(ProximityPacket);
            }
        }

    }
}
