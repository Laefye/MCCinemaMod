package com.github.laefye.cinema.client.handlers;

import com.github.laefye.cinema.client.CinemaClient;
import com.github.laefye.cinema.client.dispatcher.IDispatcherEvent;
import com.github.laefye.cinema.packet.S2CSync;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import net.minecraft.client.MinecraftClient;

public class Sync implements IDispatcherEvent {
    @Override
    public void on(WrappedPacketByteBuf buf, MinecraftClient client, CinemaClient cinema) {
        var packet = new S2CSync();
        packet.parse(buf);

        client.execute(() -> {
            var screen = cinema.screenCollection.get(packet.uuid);
            if (screen != null) {
                screen.player.setMillis(packet.millis);
            }
        });
    }
}
