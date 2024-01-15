package com.mygdx.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Handlers.MyTimer;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants.*;
import com.mygdx.Tools.Constants;
import java.util.EnumSet;

public class Player extends Entity implements Subscriber {
    private final MyTimer timer;
    private final MyResourceManager resourceManager;
    private final World world;
    private int wallState;  // -1 for left, 1 for right, 0 for none
    private int lives;
    private Vector2 currCheckPoint;
    private MSTATE movementState;
    private ASTATE currAState;     // Current animation state
    private ASTATE prevAState;     // Previous animation state
    private final EnumSet<PSTATE> playerStates;       // Set of player states

    public Player(int x, int y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, int lives) {

        super(id);
        this.timer = timer;
        this.world = world;
        this.resourceManager = myResourceManager;
        this.lives = lives;

        currCheckPoint = new Vector2(x / Constants.PPM, y / Constants.PPM);

        // Initializing states
        playerStates = EnumSet.noneOf(PSTATE.class);
        addPlayerState(PSTATE.ON_GROUND);
        currAState = ASTATE.IDLE;
        prevAState = ASTATE.IDLE;
        movementState = MSTATE.HSTILL;

        // Loading all textures
        resourceManager.loadTexture("player_run.png", "player_run");
        resourceManager.loadTexture("player_idle.png", "player_idle");
        resourceManager.loadTexture("player_jump.png", "player_jump");
        resourceManager.loadTexture("player_land.png", "player_land");
        resourceManager.loadTexture("player_fall.png", "player_fall");

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("player_idle"), 32, 32)[0], 1/5f, false);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        circleShape.setRadius(16 / Constants.PPM);
        fdef.friction = 0;  // No friction allows for players sliding next to walls
        fdef.shape = circleShape;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("player");

        //Create player hitbox
        circleShape.setRadius(15f / Constants.PPM);
        fdef.shape = circleShape;
        fdef.filter.maskBits = Constants.BIT_HAZARD;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("player_hb");

        //Create right sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(16 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("rightSensor");

        //Create left sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(-16 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("leftSensor");

        //Create bottom sensor
        polygonShape.setAsBox(3 / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -16 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("bottomSensor");

        wallState = 0;
    }

    @Override
    public void update(float delta) {

        // Capping y velocity
        if (b2body.getLinearVelocity().y < -Constants.MAX_SPEED_Y) b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -Constants.MAX_SPEED_Y));
        if (isFalling()) {
            currAState = ASTATE.FALL;
            b2body.setLinearDamping(0);
        } else if (!isStateActive(PSTATE.ON_GROUND)) {
            currAState = ASTATE.JUMP;
        }

        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }
        // Update the animation
        animation.update(delta);

        if (isStateActive(PSTATE.STUNNED) || isStateActive(PSTATE.DASHING)) movementState = MSTATE.PREV;

        switch (movementState) {
            case LEFT:
                if (isStateActive(PSTATE.ON_GROUND) && !isStateActive(PSTATE.LANDING)) currAState = ASTATE.RUN;
                facingRight = false;
                moveLeft();
                break;
            case RIGHT:
                if (isStateActive(PSTATE.ON_GROUND) && !isStateActive(PSTATE.LANDING)) currAState = ASTATE.RUN;
                facingRight = true;
                moveRight();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case PREV:
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y);
                break;
            case HSTILL:
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                if (!isStateActive(PSTATE.ON_GROUND) || isStateActive(PSTATE.LANDING)) break;
                else currAState = ASTATE.IDLE;
                break;
            case FSTILL:
                b2body.setLinearVelocity(0, 0);
                break;
        }
    }

    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_run"), 32, 32)[0], 1/14f, false);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_idle"), 32, 32)[0], 1/5f, false);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_jump"), 32, 32)[0], 1/17f, true);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_fall"), 32, 32)[0], 1/5f, true);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_land"), 32, 32)[0], 1/14f, false);
                break;
        }
    }

    public void land() {
        addPlayerState(PSTATE.ON_GROUND);
        removePlayerState(PSTATE.GLIDE_CONSUMED);
        removePlayerState(PSTATE.DASH_CONSUMED);
        addPlayerState(PSTATE.LANDING);
        currAState = ASTATE.LAND;
        timer.start(0.2f, NFLAG.LAND, this);
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
        if (isStateActive(PSTATE.WALL_GRABBED)) letGo();
        if (wallState == -1) {
            b2body.applyLinearImpulse(new Vector2(2, 3), b2body.getWorldCenter(), true);
        } else {
            b2body.applyLinearImpulse(new Vector2(-2, 3), b2body.getWorldCenter(), true);
        }
        addPlayerState(PSTATE.STUNNED);
        timer.start(0.2f, NFLAG.STUN, this);
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

    public void moveUp() {
        b2body.setLinearVelocity(0, Constants.MAX_SPEED_X / 1.5f);
    }

    public void moveDown() {
        b2body.setLinearVelocity(0, -Constants.MAX_SPEED_X);
    }

    public void attack() {

    }

    public void dash() {

        if (isStateActive(PSTATE.WALL_GRABBED)) return;

        addPlayerState(PSTATE.DASHING);
        addPlayerState(PSTATE.DASH_CONSUMED);

        // Cancel out the world's gravity and previous velocity
        b2body.setLinearDamping(0);
        world.setGravity(new Vector2(0, 0));
        b2body.setLinearVelocity(0, 0);

        boolean groundDash = false;

        // Handle direction
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(Constants.DASH_FORCE / 1.5f, -Constants.DASH_FORCE / 1.5f), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(Constants.DASH_FORCE / 1.5f, Constants.DASH_FORCE / 1.5f), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            b2body.applyLinearImpulse(new Vector2(Constants.DASH_FORCE, 0), b2body.getWorldCenter(), true);
            if (isStateActive(PSTATE.ON_GROUND)) groundDash = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(-Constants.DASH_FORCE / 1.5f, -Constants.DASH_FORCE / 1.5f), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(-Constants.DASH_FORCE / 1.5f, Constants.DASH_FORCE / 1.5f), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            b2body.applyLinearImpulse(new Vector2(-Constants.DASH_FORCE, 0), b2body.getWorldCenter(), true);
            if (isStateActive(PSTATE.ON_GROUND)) groundDash = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(0, Constants.DASH_FORCE), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(0, -Constants.DASH_FORCE), b2body.getWorldCenter(), true);
        } else if (facingRight) {
            b2body.applyLinearImpulse(new Vector2(Constants.DASH_FORCE, 0), b2body.getWorldCenter(), true);
            if (isStateActive(PSTATE.ON_GROUND)) groundDash = true;
        } else {
            b2body.applyLinearImpulse(new Vector2(-Constants.DASH_FORCE, 0), b2body.getWorldCenter(), true);
            if (isStateActive(PSTATE.ON_GROUND)) groundDash = true;
        }

        if (groundDash) timer.start(0.2f, NFLAG.GDASH, this);
        else timer.start(0.2f, NFLAG.ADASH, this);
    }

    public void glide() {
        if (isStateActive(PSTATE.GLIDE_CONSUMED)) return;
        addPlayerState(PSTATE.GLIDE_CONSUMED);
        addPlayerState(PSTATE.GLIDING);
        if (movementState == MSTATE.HSTILL) {
            timer.start(0, NFLAG.UPLIFT, this);
            return;
        }
        b2body.applyLinearImpulse(new Vector2((float) (Math.pow(b2body.getLinearVelocity().x, 3) * Math.pow(b2body.getLinearVelocity().y, 2)), 0), b2body.getWorldCenter(), true);
        float invertG = b2body.getLinearVelocity().y * -3;
        world.setGravity(new Vector2(0, invertG > 15 ? 15 : invertG));
        timer.start(0.5f, NFLAG.UPLIFT, this);
    }

    public void grab() {
        if (isStateActive(PSTATE.STUNNED)) return;
        b2body.setLinearDamping(0);
        removePlayerState(PSTATE.GLIDE_CONSUMED);
        addPlayerState(PSTATE.WALL_GRABBED);
        world.setGravity(new Vector2(0, 0));
        b2body.setLinearVelocity(0, 0);
    }

    public void letGo() {
        removePlayerState(PSTATE.WALL_GRABBED);
        movementState = MSTATE.PREV;
        world.setGravity(new Vector2(0, -Constants.G_ENHANCED));
        b2body.applyLinearImpulse(new Vector2(0, -0.5f), b2body.getWorldCenter(), true);
    }

    @Override
    public void die() {
        lives--;
        if (lives == 0) return;     // Handle later
        respawn();
    }

    public void respawn() {
        b2body.setTransform(currCheckPoint, b2body.getAngle());
    }

    @Override
    public void notify(NFLAG flag) {
        switch (flag){
            case STUN:
                removePlayerState(PSTATE.STUNNED);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MSTATE.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MSTATE.LEFT;
                break;
            case UPLIFT:
                if (!isStateActive(PSTATE.ON_GROUND) && isStateActive(PSTATE.GLIDING)) world.setGravity(new Vector2(0, -Constants.G_GLIDING));
                else if (isStateActive(PSTATE.ON_GROUND)) land();
                else world.setGravity(new Vector2(0, -Constants.G_ENHANCED));
                break;
            case ADASH:
                if (!isStateActive(PSTATE.ON_GROUND)) world.setGravity(new Vector2(0, -Constants.G_ENHANCED));
                else world.setGravity(new Vector2(0, -Constants.G));
                removePlayerState(PSTATE.DASHING);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MSTATE.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MSTATE.LEFT;
                break;
            case GDASH:
                world.setGravity(new Vector2(0, -Constants.G));
                removePlayerState(PSTATE.DASHING);
                timer.start(1, NFLAG.DASH_COOLDOWN, this);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MSTATE.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MSTATE.LEFT;
                break;
            case DASH_COOLDOWN:
                removePlayerState(PSTATE.DASH_CONSUMED);
                break;
            case LAND:
                removePlayerState(PSTATE.LANDING);
                break;
        }
    }

    public boolean isFalling() { return b2body.getLinearVelocity().y < 0; }

    public void setWallState(int wallState) { this.wallState = wallState; }

    public int getWallState() { return wallState; }

    public void setMovementState(Constants.MSTATE direction) { this.movementState = direction; }

    public MSTATE getMovementState() { return movementState; }

    public void addPlayerState(PSTATE state) { playerStates.add(state); }

    public void removePlayerState(PSTATE state) { playerStates.remove(state); }

    public boolean isStateActive(PSTATE state) { return playerStates.contains(state); }

    public void setCheckPoint(Vector2 position) { currCheckPoint = position; }

}
