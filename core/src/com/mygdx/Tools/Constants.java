package com.mygdx.Tools;

public class Constants {
    public static final int TILE_SIZE = 16;
    public static final float PPM = 100;
    public static final float MAX_SPEED = 1.6F;
    public static final float G = 13;
    public static final float G_ENHANCED = 15;
    public static final float G_GLIDING = 7;
    public static final float DASH_FORCE = 5;
    // Movement state Flag
    public enum MSTATE {
        UP, DOWN, LEFT, RIGHT,
        PREV, // Keep previous direction
        HSTILL, // Horizontal still
        FSTILL; // Full still
    }
    // Player state flag
    public enum PSTATE {
        DASHING, STUNNED, WALL_GRABBED, ON_GROUND, GLIDING, GLIDE_CONSUMED, DASH_CONSUMED, FACING_RIGHT
    }
    // Animation state flag
    public enum ASTATE {
        RRUN, LRUN, JUMP, RSTAND, LSTAND
    }
    // Notification Flag
    public enum NFLAG {
        STUN, UPLIFT,
        ADASH, // Air dash
        GDASH, // Ground dash
        DASH_COOLDOWN
    }

    // Bits for collision masking
    public static final short BIT_GROUND = 2;   // Includes ground and walls (because sometimes the ground can be a wall)
    public static final short BIT_HAZARD = 4;
}
