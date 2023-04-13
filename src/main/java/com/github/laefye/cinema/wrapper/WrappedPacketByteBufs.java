package com.github.laefye.cinema.wrapper;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class WrappedPacketByteBufs {
    public static WrappedPacketByteBuf create() {
        var buf = new WrappedPacketByteBuf();
        buf.buf = PacketByteBufs.create();
        return buf;
    }

    public static WrappedPacketByteBuf wrap(PacketByteBuf buff) {
        var buf = new WrappedPacketByteBuf();
        buf.buf = buff;
        return buf;
    }
}
