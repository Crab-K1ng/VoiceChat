package io.github.CrabK1ng.Proximity.Utils;

import io.netty.buffer.ByteBuf;

public class BufferUtil {

    public static byte[] toByteArray(ByteBuf byteBuf) {
        if (byteBuf.hasArray()) return byteBuf.array();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        int size = byteBuf.readableBytes();
//        for (int i = 0; i < size; i++) {
//            bytes[i] = byteBuf.readByte();
//        }
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static void printOutByteArray(byte[] bytes) {
        System.out.println("Size: " + bytes.length);
        for (byte bite: bytes) {
            System.out.printf("%02X ", bite);
        }
        System.out.println();
    }

}
