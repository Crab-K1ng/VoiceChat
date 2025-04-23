package io.github.CrabK1ng.Proximity.networking.packets;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.core.loader.meta.EnvType;
import io.github.CrabK1ng.Proximity.networking.ProxNetIdentity;
import io.github.CrabK1ng.Proximity.networking.Server;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;
import io.github.CrabK1ng.Proximity.serialization.IKeylessDeserializer;
import io.github.CrabK1ng.Proximity.serialization.IKeylessSerializer;
import io.github.CrabK1ng.Proximity.threads.ThreadsManger;

import java.io.IOException;

public class AudioPacket extends ProxPacket {
    OpusDecoderHandler decoder;
    String userID;
    Vector3 position = new Vector3();
    byte[] buffer;

    public AudioPacket() {}

    public AudioPacket(String userID, byte[] buffer, Vector3 position) {
        this.userID = userID;
        this.buffer = buffer;
        this.position = position;
    }

    @Override
    public void read(IKeylessDeserializer deserializer) throws IOException {
        this.userID = deserializer.readString();
        this.position = deserializer.readVector3();
        this.buffer = deserializer.readByteArrayAsNative();
    }

    @Override
    public void write(IKeylessSerializer serializer) throws IOException {
        serializer.writeString(userID);
        serializer.writeVector3(position);
        serializer.writeByteArray(buffer);
    }

    @Override
    public void handle(EnvType type, ProxNetIdentity proxNetIdentity) {
        if (buffer != null){
            if (com.github.puzzle.core.Constants.SIDE == EnvType.CLIENT) {
                ThreadsManger.speakersThread.add(buffer);
                if (!ThreadsManger.speakersThread.isRunning) ThreadsManger.speakersThread.run();
            } else {
                try {
                    Server.send(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
