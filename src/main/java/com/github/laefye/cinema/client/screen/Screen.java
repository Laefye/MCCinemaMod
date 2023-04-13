package com.github.laefye.cinema.client.screen;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.mediaplayer.IMediaPlayer;
import com.github.laefye.cinema.mediaplayer.VLCMediaPlayer;
import com.github.laefye.cinema.mediaplayer.events.IEventMediaStop;
import com.github.laefye.cinema.types.ScreenInfo;

public class Screen {
    public int width = 1280;
    public int height = 720;
    public IMediaPlayer player;
    public ScreenTexture playerTexture;
    public ScreenRenderer renderer;
    public ScreenInfo info;

    public Screen(ScreenInfo info) {
        this.info = info;
        playerTexture = new ScreenTexture(width, height);
        player = new VLCMediaPlayer(playerTexture);
        renderer = new ScreenRenderer(this);
    }

    public boolean isPlaying = false;

    public void play(String filename) {
        if (filename.equals("")) {
            player.stop();
            isPlaying = false;
            return;
        }
        player.play(filename);
        isPlaying = true;
    }

    public void free() {
        player.free();
        playerTexture.free();
    }

    public double sound(double distance) {
        return Math.max(0, 1 - distance / info.distance);
    }
}
