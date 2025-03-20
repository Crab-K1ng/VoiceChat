package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ByteArray;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.github.CrabK1ng.Proximity.Constants;
import io.github.CrabK1ng.Proximity.Proximity;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

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
            this.buffer = in.array();
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
            int sampleRate = 8000; // Opus supports 8000, 12000, 16000, 24000, and 48000
            int channels = 1;       // Mono (1) or Stereo (2)
            if (buffer != null /*&& !Objects.equals(identity.getPlayer().getUsername(), user)*/) {
                OpusDecoderHandler decoder = null;
                try {
                    decoder = new OpusDecoderHandler(sampleRate, channels);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                short[] decoded;
                try {
                    decoded = decoder.decode(buffer);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode Opus data", e);
                }

                byte[] byteDecoded = Proximity.shortToByteArray(decoded);
                Constants.LOGGER.info("Writing to speaker");
                Constants.LOGGER.info(byteDecoded.length);
                Proximity.speakers.write(byteDecoded, 0, byteDecoded.length);
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
