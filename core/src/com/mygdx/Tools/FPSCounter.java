package com.mygdx.Tools;

public class FPSCounter {

    private float timeSinceLastTick = 0;
    private int framesThisTick = 0, framesLastTick = 0;
    private boolean secondHalf = false;
    private int fps = 0;

    public FPSCounter() {}

    public int getFramesPerSecond() {
        return fps;
    }

    public void update(float delta) {
        timeSinceLastTick += delta;
        framesThisTick++;

        if (timeSinceLastTick >= 0.5F) { // every 0.5 seconds
            // Update FPS counter per second
            if (secondHalf) {
                fps = framesThisTick + framesLastTick;
            } else {
                framesLastTick = framesThisTick;
            }
            secondHalf = !secondHalf;

            framesThisTick = 0;
            timeSinceLastTick = 0;
        }
    }

}

