package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ByteArray;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.github.CrabK1ng.Proximity.Proximity;
import io.github.CrabK1ng.Proximity.opus.OpusCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

public class ProximityPacket extends GamePacket implements IProximityPacket {

    String user;
    Vector3 position;
    byte[] buffer;

    public ProximityPacket(){}

    @Override
    public void receive(ByteBuf byteBuf) {}

    public ProximityPacket(byte[] buffer, Vector3 position, String user) {
        this.buffer = buffer;
        this.position = position;
        this.user = user;
    }

    @Override
    public void receive(ByteBuf in, Vector3 position, String user) {
        if (in.hasArray()) {
            byte[] array = in.array();
            this.buffer = array;
            this.position = position;
            this.user = user;
        } else {
            byte[] array = new byte[in.readableBytes()];
            in.getBytes(in.readerIndex(), array);
            this.buffer = array;
            this.position = position;
            this.user = user;
            
        }
    }

    @Override
    public void write() {
        this.writeByteArray(ByteArray.with(this.buffer));
        this.writeVector3(this.position);
        this.writeString(this.user);
    }

    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.isClient()) {
            if (buffer != null /*&& !Objects.equals(identity.getPlayer().getUsername(), user)*/) {
                OpusCodec codec = OpusCodec.createDefault();
                byte[] decoded = codec.decodeFrame(buffer);
                Proximity.speakers.write(decoded, 0, buffer.length);
            }
        }
        if (identity.isServer()){
            if (buffer != null) {
                ProximityPacket ProximityPacket = new ProximityPacket(buffer.clone(),this.position,this.user);
                ServerSingletons.SERVER.broadcastToAll(ProximityPacket);
            }
        }

    }

    @Override
    public void receive(ByteBuf in, Vector3 position) {
        
    }
}
