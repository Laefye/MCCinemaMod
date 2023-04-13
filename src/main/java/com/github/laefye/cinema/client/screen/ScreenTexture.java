package com.github.laefye.cinema.client.screen;

import com.github.laefye.cinema.Cinema;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ScreenTexture {
    public int textureId;
    private int _width;
    private int _height;

    private ByteBuffer _buffer;

    public ScreenTexture(int width, int height) {
        textureId = newTexture();
        _buffer = BufferUtils.createByteBuffer(width * height * 3);
        _width = width;
        _height = height;
    }

    public int newTexture() {
        var id = GL11.glGenTextures();

        RenderSystem.bindTexture(id);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGB, _width, _height, 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        return id;
    }

    public void setBuffer(IntBuffer buffer) {
        _buffer.clear();
        for (int i = 0; i < _width * _height; i++) {
            var value = buffer.get();
            _buffer.put((byte) ((value >> 16) & 0xFF));
            _buffer.put((byte) ((value >> 8) & 0xFF));
            _buffer.put((byte) (value & 0xFF));
        }
        _buffer.flip();
    }


    public void paint() {
        paint(textureId);
    }

    public void paint(int id) {
        RenderSystem.bindTexture(id);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGB, _width, _height, 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, _buffer);
        GL11.glFinish();
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }

    public void free() {
        RenderSystem.deleteTexture(textureId);
    }
}
