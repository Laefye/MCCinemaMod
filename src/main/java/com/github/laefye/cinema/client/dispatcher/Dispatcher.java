package com.github.laefye.cinema.client.dispatcher;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.client.CinemaClient;
import com.github.laefye.cinema.packet.AbstractPacket;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Dispatcher {
    private static class DispatcherEntry {
        public AbstractPacket.PacketCommand command;
        public IDispatcherEvent event;

        public DispatcherEntry(AbstractPacket.PacketCommand command, IDispatcherEvent event) {
            this.command = command;
            this.event = event;
        }
    }

    private final ArrayList<DispatcherEntry> events = new ArrayList<>();

    public void add(AbstractPacket.PacketCommand command, IDispatcherEvent event) {
        events.add(new DispatcherEntry(command, event));
    }

    public void call(WrappedPacketByteBuf buf, MinecraftClient client, CinemaClient cinema) {
        var command = AbstractPacket.PacketCommand.values()[buf.readInt()];
        Cinema.LOGGER.info("net: " + command);
        for (var entry : events) {
            if (command == entry.command) {
                Cinema.LOGGER.info("net-handle!");
                entry.event.on(buf, client, cinema);
            }
        }
    }
}
