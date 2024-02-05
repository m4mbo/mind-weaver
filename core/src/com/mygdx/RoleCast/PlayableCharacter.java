package com.mygdx.RoleCast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Subscriber;
import com.mygdx.Helpers.Constants;
import java.util.EnumSet;

public abstract class PlayableCharacter extends Entity implements Subscriber {
    protected final MyTimer timer;
    protected final World world;
    protected int wallState;  // -1 for left, 1 for right, 0 for none
    protected Constants.MSTATE movementState;
    protected Constants.ASTATE currAState;     // Current animation state
    protected Constants.ASTATE prevAState;     // Previous animation state
    protected final EnumSet<Constants.PSTATE> playerStates;       // Set of player states
    protected PlayableCharacter bullseye;
    protected CharacterCycle characterCycle;
    protected VisionMap visionMap;
    protected int floorContacts; // Number of contacts with the floor to avoid anomalies
    private int airIterations;

    public PlayableCharacter(World world, int id, MyTimer timer, MyResourceManager myResourceManager, CharacterCycle characterCycle, VisionMap visionMap) {

        super(id, myResourceManager);
        this.timer = timer;
        this.world = world;
        this.bullseye = null;
        this.characterCycle = characterCycle;
        this.visionMap = visionMap;

        // Initializing states
        playerStates = EnumSet.noneOf(Constants.PSTATE.class);
        addPlayerState(Constants.PSTATE.ON_GROUND);
        currAState = Constants.ASTATE.IDLE;
        prevAState = Constants.ASTATE.IDLE;
        movementState = Constants.MSTATE.HSTILL;

        wallState = 0;
        floorContacts = 0;
        airIterations = 0;
    }

    public void update(float delta) {

        // Capping y velocity
        if (b2body.getLinearVelocity().y < -Constants.MAX_SPEED_Y)
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -Constants.MAX_SPEED_Y));

        if (!isStateActive(Constants.PSTATE.ON_GROUND)) {
            airIterations++;
        } else {
            airIterations = 0;
        }

        // Animation priority
       if (airIterations >= 5) {
            if (isFalling()) {
                currAState = Constants.ASTATE.FALL;
                b2body.setLinearDamping(0);
            } else {
                currAState = Constants.ASTATE.JUMP;
            }
        }

        if (isStateActive(Constants.PSTATE.STUNNED)) movementState = Constants.MSTATE.PREV;

        switch (movementState) {
            case LEFT:
                if (isStateActive(Constants.PSTATE.ON_GROUND) && !isStateActive(Constants.PSTATE.LANDING)) currAState = Constants.ASTATE.RUN;
                facingRight = false;
                moveLeft();
                break;
            case RIGHT:
                if (isStateActive(Constants.PSTATE.ON_GROUND) && !isStateActive(Constants.PSTATE.LANDING)) currAState = Constants.ASTATE.RUN;
                facingRight = true;
                moveRight();
                break;
            case PREV:
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y);
                break;
            case HSTILL:
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                if (!isStateActive(Constants.PSTATE.ON_GROUND) || isStateActive(Constants.PSTATE.LANDING)) break;
                else currAState = Constants.ASTATE.IDLE;
                break;
            case FSTILL:
                b2body.setLinearVelocity(0, 0);
                break;
        }

        if (isStateActive(Constants.PSTATE.ATTACKING)) currAState = Constants.ASTATE.ATTACK;

        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }
        // Update the animation
        animation.update(delta);
    }

    public void land() {
        addPlayerState(Constants.PSTATE.ON_GROUND);
        addPlayerState(Constants.PSTATE.LANDING);
        if (airIterations >= 5) currAState = Constants.ASTATE.LAND;
        timer.start(0.2f, "land", this);
        world.setGravity(new Vector2(0, -Constants.G));
        b2body.setLinearDamping(0);
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
    }

    public void fall() {
        world.setGravity(new Vector2(0, -Constants.G_ENHANCED));
        if (!isFalling()) b2body.setLinearDamping(12);   // If not falling, set linear damping to regulate height
    }

    public void wallJump() {
        if (wallState == -1) {
            b2body.applyLinearImpulse(new Vector2(2, 3), b2body.getWorldCenter(), true);
        } else {
            b2body.applyLinearImpulse(new Vector2(-2, 3), b2body.getWorldCenter(), true);
        }
        addPlayerState(Constants.PSTATE.STUNNED);
        timer.start(0.2f, "stun", this);
    }

    public void moveRight() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(Constants.MAX_SPEED_X, b2body.getLinearVelocity().y);
    }

    public void moveLeft() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(-0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(-Constants.MAX_SPEED_X, b2body.getLinearVelocity().y);
    }

    @Override
    public void notify(String flag) {
        switch (flag) {
            case "stun":
                removePlayerState(Constants.PSTATE.STUNNED);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = Constants.MSTATE.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = Constants.MSTATE.LEFT;
                break;
            case "land":
                removePlayerState(Constants.PSTATE.LANDING);
                break;
            case "stop":
                movementState = Constants.MSTATE.HSTILL;
                break;
        }
    }

    public void looseControl() {
        movementState = Constants.MSTATE.PREV;
        timer.start(0.4f, "stop", this);
    }

    public boolean isFalling() { return b2body.getLinearVelocity().y < 0; }

    public void setWallState(int wallState) { this.wallState = wallState; }

    public int getWallState() { return wallState; }

    public void setMovementState(Constants.MSTATE direction) { this.movementState = direction; }

    public Constants.MSTATE getMovementState() { return movementState; }

    public void addPlayerState(Constants.PSTATE state) { playerStates.add(state); }

    public void removePlayerState(Constants.PSTATE state) { playerStates.remove(state); }

    public boolean isStateActive(Constants.PSTATE state) { return playerStates.contains(state); }

    public void setBullseye(PlayableCharacter character) {
        bullseye = character;
        characterCycle.updateCycle();
    }

    public PlayableCharacter getBullseye() { return bullseye; }

    public void increaseFloorContact() {
        if (floorContacts == 0) land();
        floorContacts++;
    }

    public void decreaseFloorContact() {
        floorContacts--;
        if (floorContacts == 0) {
            removePlayerState(Constants.PSTATE.ON_GROUND);
        }
    }
}
