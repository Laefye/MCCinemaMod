package com.github.laefye.cinema.mediaplayer;

public interface IMediaPlayer {
    void setMillis(long millis);
    void pause(boolean pauseState);
    boolean pause();
    void play(String filename);
    void stop();
    void free();

    void render(boolean status);
    boolean render();

    SimpleEventSystem eventSystem();

    void looping(boolean loop);

    void sound(double f);

    boolean isPlaying();
}
