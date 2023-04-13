package com.github.laefye.cinema.wrapper;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class WrappedPacketByteBuf {
    public PacketByteBuf buf;

    public long readLong() {
        return buf.readLong();
    }

    public void writeLong(long value) {
        buf.writeLong(value);
    }

    public int readInt() {
        return buf.readInt();
    }

    public void writeInt(int value) {
        buf.writeInt(value);
    }

    public void writeBytes(byte[] bytes, int beginIndex, int length) {
        for (int i = beginIndex; i < beginIndex + length; i++) {
            buf.writeByte(bytes[i]);
        }
    }

    public void readBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buf.readByte();
        }
    }

    public void writeFloat(float value) {
        buf.writeFloat(value);
    }

    public void writeDouble(double value) {
        buf.writeDouble(value);
    }

    public float readFloat() {
        return buf.readFloat();
    }

    public double readDouble() {
        return buf.readDouble();
    }
}
