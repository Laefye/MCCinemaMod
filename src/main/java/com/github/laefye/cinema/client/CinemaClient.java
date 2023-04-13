package com.github.laefye.cinema.client;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.client.dispatcher.Dispatcher;
import com.github.laefye.cinema.client.handlers.InitScreens;
import com.github.laefye.cinema.client.handlers.Sync;
import com.github.laefye.cinema.client.handlers.UrlSync;
import com.github.laefye.cinema.client.screen.ScreenCollection;
import com.github.laefye.cinema.packet.*;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBufs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class CinemaClient implements ClientModInitializer {
    public final ScreenCollection screenCollection = new ScreenCollection();
    public static CinemaClient INSTANCE;
    public Dispatcher dispatcher = new Dispatcher();
    public static long PROTOCOL = 1;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            client.execute(() -> {
                screenCollection.free();
                var packet = new C2SInitScreens();
                packet.init(PROTOCOL);
                ClientPlayNetworking.send(Cinema.CINEMA_CHANNEL, packet.getBuffer().buf);
            });
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(screenCollection::render);

        ClientTickEvents.END_CLIENT_TICK.register(screenCollection::tick);

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            screenCollection.stop();
        });

        dispatcher.add(AbstractPacket.PacketCommand.S2C_INIT_SCREENS, new InitScreens());
        dispatcher.add(AbstractPacket.PacketCommand.S2C_SYNC, new Sync());
        dispatcher.add(AbstractPacket.PacketCommand.S2C_URL, new UrlSync());

        ClientPlayNetworking.registerGlobalReceiver(Cinema.CINEMA_CHANNEL, (client, handler, in, responseSender) -> {
            var buf = WrappedPacketByteBufs.wrap(in);
            dispatcher.call(buf, client, this);
        });
    }
}
