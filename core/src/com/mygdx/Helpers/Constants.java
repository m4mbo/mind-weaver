package com.mygdx.Helpers;

public class Constants {
    public static final int TILE_SIZE = 10;
    public static final float PPM = 100;
    public static final float MAX_SPEED_X = 1.2f;
    public static final float MAX_SPEED_Y = 5f;
    public static final float G = 11;
    public static final float G_ENHANCED = 13;
    // Movement state Flag
    public enum MSTATE {
        LEFT, RIGHT,
        PREV, // Keep previous direction
        HSTILL, // Horizontal still
        FSTILL; // Full still
    }
    // Player state flag
    public enum PSTATE {
        STUNNED, ON_GROUND, LANDING,
    }
    // Animation state flag
    public enum ASTATE {
        RUN, JUMP, IDLE, FALL, LAND
    }

    // Bits for collision masking
    public static final short BIT_GROUND = 2;   // Includes ground and walls (because sometimes the ground can be a wall)
    public static final short BIT_HAZARD = 4;
    public static final short BIT_CHECKPOINT = 8;
    public static final short BIT_GOBLIN = 16;
    public static final short BIT_MAGE = 32;
    public static final short BIT_ROV = 64;
    public static final short BIT_SUPPORT = 128;
    public static final short BIT_FEET = 256;
}