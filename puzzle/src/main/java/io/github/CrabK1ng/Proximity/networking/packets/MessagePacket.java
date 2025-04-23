package io.github.CrabK1ng.Proximity.networking.packets;

import com.github.puzzle.core.loader.meta.EnvType;
import io.github.CrabK1ng.Proximity.networking.ProxNetIdentity;
import io.github.CrabK1ng.Proximity.serialization.IKeylessDeserializer;
import io.github.CrabK1ng.Proximity.serialization.IKeylessSerializer;

import java.io.IOException;

public class MessagePacket extends ProxPacket {

    String message;

    public MessagePacket() {}

    public MessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void read(IKeylessDeserializer deserializer) throws IOException {
        message = deserializer.readString();
    }

    @Override
    public void write(IKeylessSerializer serializer) throws IOException {
        serializer.writeString(message);
    }

    @Override
    public void handle(EnvType type, ProxNetIdentity proxNetIdentity) {
        System.out.println(message);
    }

}
