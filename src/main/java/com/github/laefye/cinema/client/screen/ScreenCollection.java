package com.github.laefye.cinema.client.screen;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.types.ScreenInfo;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.dimension.DimensionTypes;

import java.awt.*;
import java.util.HashMap;

public class ScreenCollection {
    private final HashMap<String, Screen> _screens = new HashMap<>();


    public Screen addScreen(String uuid, ScreenInfo info) {
        var screen = new Screen(info);
        _screens.put(uuid, screen);
        return screen;
    }

    public void render(WorldRenderContext context) {
        if (context.world().getDimensionKey() != DimensionTypes.OVERWORLD) {
            return;
        }

        var cameraPos = context.camera().getPos();
        for (var screen : _screens.values()) {
            if (cameraPos.distanceTo(screen.info.location()) < screen.info.distance) {
                screen.renderer.afterTranslucent(context);
            }
        }
    }

    private int _tick = 0;

    public void tick(MinecraftClient client) {
        var player = client.player;
        for (var screen : _screens.values()) {
            if (screen.isPlaying && player != null && _tick > 5) {
                if (client.world != null && client.world.getDimensionKey() != DimensionTypes.OVERWORLD) {
                    screen.player.sound(0);
                    continue;
                }
                var d = screen.sound(player.getPos().distanceTo(screen.info.location())) * client.options.getSoundVolume(SoundCategory.MASTER) * client.options.getSoundVolume(SoundCategory.RECORDS);
                screen.player.sound(d);
            }
        }
        if (_tick > 5) {
            _tick = 0;
        }
        _tick++;
    }

    public void free() {
        for (var screen : _screens.values()) {
            screen.free();
        }
        _screens.clear();
    }

    public Screen get(String uuid) {
        if (_screens.containsKey(uuid)) {
            return _screens.get(uuid);
        }
        return null;
    }

    public void stop() {
        for (var screen : _screens.values()) {
            Cinema.LOGGER.info("Trying to stop: " + screen.info.x);
            screen.player.stop();
        }
    }
}
