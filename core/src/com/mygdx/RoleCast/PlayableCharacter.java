package com.mygdx.RoleCast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Logic.MyTimer;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.ShapeDrawer;
import java.util.EnumSet;

public abstract class PlayableCharacter extends Entity implements Subscriber {
    protected final MyTimer timer;
    protected final World world;
    protected int wallState;  // -1 for left, 1 for right, 0 for none
    protected Constants.MSTATE movementState;
    protected Constants.ASTATE currAState;     // Current animation state
    protected Constants.ASTATE prevAState;     // Previous animation state
    protected final EnumSet<Constants.PSTATE> playerStates;       // Set of player states
    protected PlayableCharacter target;
    private boolean collision;      // Helper boolean variable for rayCasting
    private final ShapeDrawer shapeDrawer;
    private final EntityHandler entityHandler;

    public PlayableCharacter(World world, int id, MyTimer timer, MyResourceManager myResourceManager, ShapeDrawer shapeDrawer, EntityHandler entityHandler) {

        super(id, myResourceManager);
        this.timer = timer;
        this.world = world;
        this.target = null;
        this.entityHandler = entityHandler;

        // Initializing states
        playerStates = EnumSet.noneOf(Constants.PSTATE.class);
        addPlayerState(Constants.PSTATE.ON_GROUND);
        currAState = Constants.ASTATE.IDLE;
        prevAState = Constants.ASTATE.IDLE;
        movementState = Constants.MSTATE.HSTILL;

        this.shapeDrawer = shapeDrawer;

        collision = false;

        wallState = 0;
    }

    public void loadSprites() {}

    public void land() {
        addPlayerState(Constants.PSTATE.ON_GROUND);
        addPlayerState(Constants.PSTATE.LANDING);
        currAState = Constants.ASTATE.LAND;
        timer.start(0.2f, "land", this);
        world.setGravity(new Vector2(0, -Constants.G));
        b2body.setLinearDamping(0);
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 4.2f), b2body.getWorldCenter(), true);
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

    public void removeTarget() {
        removePlayerState(Constants.PSTATE.EOT);
        target = null;
    }

    public void sendSignal() {
        final Vector2 targetPos = target.getPosition();
        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 vector2, Vector2 vector21, float v) {
                if (fixture.getUserData().equals("ground") || fixture.getUserData().equals("hazard")) {
                    PlayableCharacter.this.collision = true;
                    return 0;
                }
                return 1;
            }
        };
        world.rayCast(callback, b2body.getPosition() , targetPos);
        if (!collision) {
            establishConnection();
        } else {
            removePlayerState(Constants.PSTATE.EOT);
            if (entityHandler.characterRollback()) target.looseControl();

            collision = false;
        }
    }

    public void establishConnection() {
        addPlayerState(Constants.PSTATE.EOT);
        shapeDrawer.drawLine(target.getPosition(), b2body.getPosition(), 2);
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

    public void setTarget(PlayableCharacter character) { target = character; }

    public PlayableCharacter getTarget() { return target; }
}
