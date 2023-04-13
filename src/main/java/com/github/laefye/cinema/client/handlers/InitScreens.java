package com.github.laefye.cinema.client.handlers;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.client.CinemaClient;
import com.github.laefye.cinema.client.dispatcher.IDispatcherEvent;
import com.github.laefye.cinema.packet.C2SSync;
import com.github.laefye.cinema.packet.S2CInitScreens;
import com.github.laefye.cinema.mediaplayer.events.IEventMediaReady;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class InitScreens implements IDispatcherEvent {
    @Override
    public void on(WrappedPacketByteBuf buf, MinecraftClient client, CinemaClient cinema) {
        var packet = new S2CInitScreens();
        packet.parse(buf);
        client.execute(() -> {
            if (Cinema.disableMod && client.player != null) {
                var os = Cinema.getOperatingSystemType();
                if (os == Cinema.OS.WINDOWS) {
                    client.player.sendMessage(Text.translatable("cinema.vlc.windows.error").formatted(Formatting.RED));
                } else if (os == Cinema.OS.LINUX) {
                    client.player.sendMessage(Text.translatable("cinema.vlc.linux.error").formatted(Formatting.RED));
                } else if (os == Cinema.OS.MACOS) {
                    client.player.sendMessage(Text.translatable("cinema.vlc.macos.error").formatted(Formatting.RED));
                }
                return;
            }
            for (var i : packet.serverScreenInfoList) {
                var screen = cinema.screenCollection.addScreen(i.uuid, i.info);
                screen.player.eventSystem().add((IEventMediaReady) () -> {
                    if (screen.isPlaying) {
                        var syncPacket = new C2SSync();
                        syncPacket.init(i.uuid);
                        ClientPlayNetworking.send(Cinema.CINEMA_CHANNEL, syncPacket.getBuffer().buf);
                    }
                });
            }
        });
    }
}
