package com.mygdx.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Custom animation class
public class Animation {

    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;
    private boolean loopLastFrame;

    public Animation() {}

    public Animation(TextureRegion[] frames) {
        this(frames, 1/12f, false);
    }

    public Animation(TextureRegion[] frames, float delay, boolean loopLastFrame) {
        setFrames(frames, delay, loopLastFrame);
    }

    public void setFrames(TextureRegion[] frames, float delay, boolean loopLastFrame) {
        this.frames = frames;
        this.delay = delay;
        this.loopLastFrame = loopLastFrame;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;
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
            timesPlayed++;
        }
    }

    public TextureRegion getFrame() { return frames[currentFrame]; }
    public int getTimesPlayed() { return timesPlayed; }

}
