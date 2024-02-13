package com.mygdx.Helpers;

public class Constants {
    public static final int TILE_SIZE = 10;
    public static final float PPM = 100;
    public static final float MAX_SPEED_X = 1.2f;
    public static final float MAX_SPEED_Y = 5f;
    public static final float TEXT_SPEED = 8f;
    public static final float KNOCKBACK_SCALE = 1f;
    public static final float G = 11;
    public static final float G_ENHANCED = 13;
    public static final float BUTTON_WIDTH = 345;
    public static final float BUTTON_HEIGHT = 190.5f;

    // Movement state Flag
    public enum MSTATE {
        LEFT, RIGHT,
        PREV, // Keep previous direction
        HSTILL, // Horizontal still
        FSTILL; // Full still
    }

    // Player state flag
    public enum PSTATE {
        STUNNED, ON_GROUND, LANDING, ATTACKING, ATTACK_STUN, HIT, DYING
    }
    // Animation state flag
    public enum ASTATE {
        RUN, JUMP, IDLE, FALL, LAND, OPEN, CLOSED, ATTACK, DEATH
    }

    public enum SCREEN_TYPE {
        START, RESUME, LEVELS, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, SETTINGS, MENU, LEVEL_COMPLETE, EXIT, RESTART

    }

    // Bits for collision masking
    public static final short BIT_GROUND = 2;   // Includes ground and walls (because sometimes the ground can be a wall)
    public static final short BIT_HAZARD = 4;
    public static final short BIT_GOBLIN = 8;
    public static final short BIT_MAGE = 16;
    public static final short BIT_ROV = 32;
    public static final short BIT_SUPPORT = 64;
    public static final short BIT_FEET = 128;
    public static final short BIT_LIGHT = 256;
    public static final short BIT_INTERACT = 512;
}
