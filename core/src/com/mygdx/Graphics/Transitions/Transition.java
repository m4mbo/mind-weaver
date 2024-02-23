package com.mygdx.Graphics.Transitions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public abstract class Transition {

    public float duration;      // Duration of transition in seconds
    public float timePassed;    // Time passed since transition creation
    public Matrix4 screenProjection;    // Screen projection from parent screen

    public Transition(float duration, Matrix4 screenProjection) {
        this.duration = duration;
        this.timePassed = 0;
        this.screenProjection = screenProjection;
    }

    public void render(SpriteBatch batch, float delta) { }

    public boolean isDone() {
        return duration >= timePassed;
    }

}
