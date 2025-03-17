package io.github.CrabK1ng.Proximity.networking;

import com.badlogic.gdx.math.Vector3;
import io.netty.buffer.ByteBuf;

public interface IProximityPacket {
    void receive(ByteBuf in, Vector3 position, String user);

    void receive(ByteBuf in, Vector3 position);
}
