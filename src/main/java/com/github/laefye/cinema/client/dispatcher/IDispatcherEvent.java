package com.github.laefye.cinema.client.dispatcher;

import com.github.laefye.cinema.client.CinemaClient;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import net.minecraft.client.MinecraftClient;

public interface IDispatcherEvent {
    void on(WrappedPacketByteBuf buf, MinecraftClient client, CinemaClient cinema);
}
