package com.mygdx.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * Custom animation class
 * Implemented to gain more control over animations
 * Limited customization from libgdx animation
 */
public class Animation {

    private TextureRegion[] frames;     // Animation frames
    private float time;
    private float delay;                // Delay in between frame rendering
    private int currentFrame;
    private boolean loopLastFrame;      // If true, keep rendering the last frame of the array once reached

    public Animation() {}

    public Animation(TextureRegion[] frames) {
        this(frames, 1/12f, false);
    }

    public Animation(TextureRegion[] frames, float delay, boolean loopLastFrame) {
        setFrames(frames, delay, loopLastFrame, 0);
    }

    public void setFrames(TextureRegion[] frames, float delay, boolean loopLastFrame, int currentFrame) {
        this.frames = frames;
        this.delay = delay;
        this.loopLastFrame = loopLastFrame;
        this.currentFrame = currentFrame;
        time = 0;
    }

    public void update(float delta) {
        if (delay <= 0) return;
        time += delta;
        while (time >= delay) {
            step();
        }
    }

    public void step() {
        time -= delay;
        currentFrame++;

        if (currentFrame == frames.length) {
            if (loopLastFrame) currentFrame--;
            else currentFrame = 0;
        }
    }

    public TextureRegion getFrame() { return frames[currentFrame]; }
    public int getCurrentFrame() { return currentFrame; }
    public int getFrameNumber() {
        if (frames == null) return 0;
        return frames.length;
    }
}
