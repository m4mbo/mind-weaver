package com.mygdx.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

public abstract class ManagedScreen implements Screen {

    // Method will depend on implementation
    public Matrix4 getProjectionMatrix() {
        return null;
    }

    public Texture screenToTexture(FrameBuffer fb) {
        fb.begin();
        render(0);
        fb.end();
        return fb.getColorBufferTexture();
    }

    public void render(float delta) {}
}
