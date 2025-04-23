package io.github.CrabK1ng.Proximity.networking.packets;

import com.github.puzzle.core.loader.meta.EnvType;
import io.github.CrabK1ng.Proximity.networking.ProxNetIdentity;
import io.github.CrabK1ng.Proximity.serialization.IKeylessDeserializer;
import io.github.CrabK1ng.Proximity.serialization.IKeylessSerializer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ProxPacket {

    public static final Map<Short, Class<? extends ProxPacket>> PACKET_MAP = new HashMap<>();
    public static final Map<Class<? extends ProxPacket>, Short> REVERSE_PACKET_MAP = new HashMap<>();

    abstract public void read(IKeylessDeserializer deserializer) throws IOException;
    abstract public void write(IKeylessSerializer serializer) throws IOException;

    /**
     * @param type the Environment Client/Server
     * @param proxNetIdentity WILL be null on client, use {@code Client.context} or {@code Client.send}
     */
    abstract public void handle(EnvType type, @Nullable ProxNetIdentity proxNetIdentity);

    static void register(int id, Class<? extends ProxPacket> packetClass) {
        PACKET_MAP.put((short) id, packetClass);
        REVERSE_PACKET_MAP.put(packetClass, (short) id);
    }

    public static void register() {
        register(0, AudioPacket.class);
    }

}
