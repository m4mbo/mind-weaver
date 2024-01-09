package com.mygdx.Tools;

public class Constants {
    public static final int TILE_SIZE = 16;
    public static final float PPM = 100;
    public static final float MAX_SPEED = 1.5F;
    public static final float G = 11;
    // Movement state Flag
    public enum MFLAG {
        UP, DOWN, LEFT, RIGHT,
        PREV, // Keep previous direction
        HSTILL, // Horizontal still
        FSTILL; // Full still
    }
    // Animation state flag
    public enum AFLAG {
        RRUN, LRUN, JUMP, RSTAND, LSTAND
    }
    // Notification Flag
    public enum NFLAG {
        STUN, UPLIFT,
        ADASH, // Air dash
        GDASH, // Ground dash
        DASH_COOLDOWN
    }
}
