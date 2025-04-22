package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ByteArray;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.savelib.IByteArray;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import io.github.CrabK1ng.Proximity.Constants;
import io.github.CrabK1ng.Proximity.Proximity;
import io.github.CrabK1ng.Proximity.Utils.Utils;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ProximityPacket extends GamePacket {
    CRBinDeserializer deserial = new CRBinDeserializer();
    String user;
    Vector3 position = new Vector3();
//    int bufferLength;
    byte[] buffer;

    public ProximityPacket(){}

    public ProximityPacket(byte[] buffer, Vector3 position, String user) {
        this.buffer = buffer;
        this.position = position;
        this.user = user;
    }

    @Override
    public void receive(ByteBuf in) {
        this.readVector3(in, this.position);
        this.user = this.readString(in);
//        this.bufferLength = this.readInt(in);
//
//        in.readBytes(buffer);

        this.deserial.prepareForRead(in.nioBuffer());

        this.buffer = this.deserial.readByteArray("audio");
    }

    @Override
    public void write() {
        this.writeVector3(this.position);
        this.writeString(this.user);
//        this.writeInt(this.buffer.length);
//        this.writeByteArray(ByteArray.with(this.buffer));
//
//

        CRBinSerializer crbinserializer = new CRBinSerializer();
        crbinserializer.writeByteArray("audio", this.buffer);
        IByteArray ibytearray = crbinserializer.toByteArray();
        this.writeByteArray(ibytearray);
    }

    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.isClient()) {
            int sampleRate = 8000; // Opus supports 8000, 12000, 16000, 24000, and 48000
            int channels = 1;       // Mono (1) or Stereo (2)
            if (buffer != null /*&& !Objects.equals(identity.getPlayer().getUsername(), user)*/) {
                OpusDecoderHandler decoder;
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

//                byte[] byteDecoded = Proximity.shortToByteArray(decoded);
                //////////////////////////////////////////////////////////
                byte[] byteDecoded = Utils.shortsToBytes(decoded);
                //////////////////////////////////////////////////////////
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

//    public void readByteArray(int bytes, ByteBuf in) {
//        byte[] byteArray = new byte[bytes];
//        for(int i = 0; i < bytes; ++i) {
//            byteArray this.readByte(in);
//        }
//
//    }
}
