package com.github.laefye.cinema.mediaplayer;

import com.github.laefye.cinema.Cinema;
import com.github.laefye.cinema.client.screen.ScreenTexture;
import com.github.laefye.cinema.mediaplayer.events.IEventMediaReady;
import com.github.laefye.cinema.mediaplayer.events.IEventMediaStop;
import net.minecraft.client.MinecraftClient;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

public class VLCMediaPlayer implements IMediaPlayer {
    private ScreenTexture _texture;
    private CallbackMediaPlayerComponent _mediaPlayer;
    private boolean _render = true;
    private final SimpleEventSystem _eventSystem = new SimpleEventSystem();

    public VLCMediaPlayer(ScreenTexture texture) {
        _texture = texture;

        var factory = new MediaPlayerFactory("");
        _mediaPlayer = new CallbackMediaPlayerComponent(factory, null, null, false, null);

        var surface = new CallbackVideoSurface(
                new BufferFormatCallback() {
                    @Override
                    public BufferFormat getBufferFormat(int i, int i1) {
                        return new RV32BufferFormat(_texture.width(), _texture.height());
                    }

                    @Override
                    public void allocatedBuffers(ByteBuffer[] byteBuffers) {

                    }
                },
                (mediaPlayer, byteBuffers, bufferFormat) -> {
                    if (_render) {
                        MinecraftClient.getInstance().execute(() -> {
                            _texture.setBuffer(byteBuffers[0].asIntBuffer());
                            _texture.paint();
                        });
                    }
                },
                false,
                VideoSurfaceAdapters.getVideoSurfaceAdapter()
        );
        _mediaPlayer.mediaPlayer().videoSurface().set(surface);
        _mediaPlayer.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventListener() {
            @Override
            public void mediaChanged(MediaPlayer mediaPlayer, MediaRef mediaRef) {
            }

            @Override
            public void opening(MediaPlayer mediaPlayer) {
            }

            @Override
            public void buffering(MediaPlayer mediaPlayer, float v) {

            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {

            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                var events = _eventSystem.get(IEventMediaStop.class);
                for (var event : events) {
                    ((IEventMediaStop) event).onStop();
                }
            }

            @Override
            public void forward(MediaPlayer mediaPlayer) {

            }

            @Override
            public void backward(MediaPlayer mediaPlayer) {

            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {

            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long l) {

            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float v) {

            }

            @Override
            public void seekableChanged(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void pausableChanged(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void titleChanged(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String s) {

            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long l) {

            }

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void scrambledChanged(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType trackType, int i) {

            }

            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType trackType, int i) {

            }

            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType trackType, int i) {

            }

            @Override
            public void corked(MediaPlayer mediaPlayer, boolean b) {

            }

            @Override
            public void muted(MediaPlayer mediaPlayer, boolean b) {

            }

            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float v) {

            }

            @Override
            public void audioDeviceChanged(MediaPlayer mediaPlayer, String s) {

            }

            @Override
            public void chapterChanged(MediaPlayer mediaPlayer, int i) {

            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                Cinema.LOGGER.info("VLCJ has errors :(");
            }

            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                var events = _eventSystem.get(IEventMediaReady.class);
                for (var event : events) {
                    ((IEventMediaReady) event).onMediaReady();
                }
            }
        });
    }

    @Override
    public void setMillis(long millis) {
        _mediaPlayer.mediaPlayer().controls().setTime(millis);
    }

    @Override
    public void pause(boolean pauseState) {

    }

    @Override
    public boolean pause() {
        return false;
    }

    @Override
    public void play(String filename) {
        _mediaPlayer.mediaPlayer().media().play(filename);
    }

    @Override
    public void stop() {
        _mediaPlayer.mediaPlayer().controls().stop();
    }

    @Override
    public void free() {
        stop();
        _mediaPlayer.release();
    }

    @Override
    public void render(boolean status) {
        _render = status;
    }

    @Override
    public boolean render() {
        return _render;
    }

    @Override
    public SimpleEventSystem eventSystem() {
        return _eventSystem;
    }

    @Override
    public void looping(boolean loop) {
        _mediaPlayer.mediaPlayer().controls().setRepeat(loop);
    }

    @Override
    public void sound(double f) {
        _mediaPlayer.mediaPlayer().audio().setVolume((int) Math.floor(f * 100));
    }

    @Override
    public boolean isPlaying() {
        return _mediaPlayer.mediaPlayer().status().isPlaying();
    }
}
