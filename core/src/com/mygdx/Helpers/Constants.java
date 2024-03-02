package com.mygdx.Helpers;

public class Constants {
    public static final int TILE_SIZE = 10; //Used to maintain constant dimension regardless of machine size

    public static final float PPM = 100;    //Pixels per meter
    public static final float MAX_SPEED_X = 1.2f;       //Max speed allowed in x-direction
    public static final float MAX_SPEED_Y = 5f;         //Max speed allowed in y-direction
    public static final float TEXT_SPEED = 20;          //Speed at which text prints in cutscene
    public static final float KNOCKBACK_SCALE = 1f;     //Move character backwards when attacked
    public static final float G = 11;                   //Normal gravity for world
    public static final float G_ENHANCED = 13;          //Enhanced gravity for falling in the world
    public static final float BUTTON_WIDTH = 250;       //Button width
    public static final float BUTTON_HEIGHT = 175.5f;   //Button height

    // Movement state Flag
    public enum MSTATE {
        LEFT, RIGHT,    //Left movement, right movement
        PREV,           // Keep previous direction
        HSTILL,         // Horizontal still
        FSTILL;         // Full still
    }

    // Player state flag
    public enum PSTATE {
        STUNNED, ON_GROUND, LANDING, ATTACKING, ATTACK_STUN, HIT, DYING
    }
    // Animation state flag
    public enum ASTATE {
        RUN, JUMP, IDLE, FALL, LAND, OPEN, CLOSED, ATTACK, DEATH
    }
    //Screen change operations to perform
    public enum SCREEN_OP {
        START, RESUME, LEVELS, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, MENU, EXIT, RESTART, CONTROLS

    }
    //Slide transition directions
    public enum SLIDE_DIR {
        SLIDE_UP, SLIDE_DOWN, SLIDE_RIGHT, SLIDE_LEFT
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
