package com.mygdx.Tools;

public class Constants {

    public static final int TILE_SIZE = 16;
    public static final float PPM = 100;
    public static final float MAX_SPEED = 1.2F;
    public static final float G = 11;
    // Movement State Flag
    public enum MFLAG {
        UP, DOWN, LEFT, RIGHT,
        PREV, // Keep previous direction
        HSTILL, // Horizontal still
        FSTILL; // Full still
    }
    // Notification Flag
    public enum NFLAG {
        STUN, UPLIFT
    }
}
