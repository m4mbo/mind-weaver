package com.mygdx.Graphics.Transitions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public abstract class Transition {

    public float duration;
    public float timePassed;
    public Matrix4 screenProjection;

    public Transition(float duration, Matrix4 screenProjection) {
        this.duration = duration;
        this.timePassed = 0;
        this.screenProjection = screenProjection;
    }

    public void render(SpriteBatch batch, float delta) {

    }

    public boolean isDone() {
        return duration >= timePassed;
    }

}
