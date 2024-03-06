package com.mygdx.Tools;

public class FPSCounter {   //track frames per second

    private float timeSinceLastTick = 0;    //stores time elapsed since last FPS update
    private int framesThisTick = 0; //counts frames since the last update
    private int framesLastTick = 0; //stores number of frames in the previous update
    private boolean secondHalf = false; //used to change between updating FPS and counters reset
    private int fps = 0;    //stores computed FPS value

    public FPSCounter() {}

    public int getFramesPerSecond() {
        return fps;
    } //get current frames per second value.

    public void update(float delta) {
        timeSinceLastTick += delta;
        framesThisTick++;

        if (timeSinceLastTick >= 0.5f) { // every 0.5 seconds
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

