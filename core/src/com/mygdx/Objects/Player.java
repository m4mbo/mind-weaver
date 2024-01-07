package com.mygdx.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.MyTimer;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants.*;
import com.mygdx.Tools.Constants;

public class Player extends Entity implements Subscriber {
    private final MyTimer timer;
    private final World world;
    private final Body b2body;
    private boolean facingRight;
    private boolean onGround;
    private boolean glideConsumed;
    private boolean dashConsumed;
    private boolean dashing;
    private int wallState;      // -1 for left, 1 for right, 0 for none
    private boolean wallGrabbed;
    private MFLAG movementState;
    private boolean stunned;
    public Player(int x, int y, World world, int id, MyTimer timer) {

        super(id);
        this.timer = timer;
        this.world = world;

        movementState = MFLAG.HSTILL;
        onGround = true;
        stunned = false;

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        circleShape.setRadius(5 / Constants.PPM);
        fdef.friction = 0;  //Friction allows for players sliding next to walls
        fdef.shape = circleShape;
        b2body.createFixture(fdef).setUserData("player");

        //Create right sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(5 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("rightSensor");

        //Create left sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(-5 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("leftSensor");

        //Create bottom sensor
        polygonShape.setAsBox(3 / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -5 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("bottomSensor");

        wallState = 0;
    }

    public void update() {
        if (stunned || dashing) movementState = MFLAG.PREV;
        switch (movementState) {
            case LEFT:
                facingRight = false;
                moveLeft();
                break;
            case RIGHT:
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
                break;
            case FSTILL:
                b2body.setLinearVelocity(0, 0);
                break;
        }
    }

    public void land() {
        onGround = true;
        glideConsumed = false;
        dashConsumed = false;
        world.setGravity(new Vector2(0, -Constants.G));
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        b2body.setLinearDamping(5);
    }

    public void wallJump() {

        if (wallGrabbed) letGo();

        if (wallState == -1) {
            b2body.applyLinearImpulse(new Vector2(2, 4), b2body.getWorldCenter(), true);
        } else {
            b2body.applyLinearImpulse(new Vector2(-2, 4), b2body.getWorldCenter(), true);
        }

        stunned = true;
        timer.start(0.2f, NFLAG.STUN, this);
    }

    public void moveRight() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(Constants.MAX_SPEED, b2body.getLinearVelocity().y);
    }

    public void moveLeft() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(-0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(-Constants.MAX_SPEED, b2body.getLinearVelocity().y);
    }

    public void moveUp() {
        b2body.setLinearVelocity(0, 0.5f);
    }

    public void moveDown() {
        b2body.setLinearVelocity(0, -0.9f);
    }

    public void attack() {

    }

    public void dash() {
        dashing = true;
        dashConsumed = true;
        world.setGravity(new Vector2(0, 0));
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(4, -4), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(4, 4), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            b2body.applyLinearImpulse(new Vector2(4, 0), b2body.getWorldCenter(), true);
            if (onGround) {
                timer.start(0.2f, NFLAG.GDASH, this);
                return;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(-4, -4), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(-4, 4), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            b2body.applyLinearImpulse(new Vector2(-4, 0), b2body.getWorldCenter(), true);
            if (onGround) {
                timer.start(0.2f, NFLAG.GDASH, this);
                return;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            b2body.applyLinearImpulse(new Vector2(0, 4), b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            b2body.applyLinearImpulse(new Vector2(0, -4), b2body.getWorldCenter(), true);
        } else if (facingRight) {
            b2body.applyLinearImpulse(new Vector2(4, 0), b2body.getWorldCenter(), true);
            if (onGround) {
                timer.start(0.2f, NFLAG.GDASH, this);
                return;
            }
        } else {
            b2body.applyLinearImpulse(new Vector2(-4, 0), b2body.getWorldCenter(), true);
            if (onGround) {
                timer.start(0.2f, NFLAG.GDASH, this);
                return;
            }
        }

        timer.start(0.2f, NFLAG.ADASH, this);
    }

    public void glide() {
        if (glideConsumed) return;
        glideConsumed = true;
        b2body.applyLinearImpulse(new Vector2((float) (Math.pow(b2body.getLinearVelocity().x, 3) * Math.pow(b2body.getLinearVelocity().y, 2)), 0), b2body.getWorldCenter(), true);
        world.setGravity(new Vector2(0, (float) -Math.pow(b2body.getLinearVelocity().y, 3)));
        timer.start(0.4f, NFLAG.UPLIFT, this);
    }

    public void grab() {
        if (stunned) return;
        glideConsumed = false;
        wallGrabbed = true;
        world.setGravity(new Vector2(0, 0));
        b2body.setLinearVelocity(0, 0);
    }

    public void letGo() {
        wallGrabbed = false;
        b2body.applyLinearImpulse(new Vector2(0, -0.1f), b2body.getWorldCenter(), true);
        world.setGravity(new Vector2(0, -Constants.G));
    }

    public void notify(NFLAG flag) {
        switch (flag){
            case STUN:
                stunned = false;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MFLAG.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MFLAG.LEFT;
                break;
            case UPLIFT:
                if (!onGround) world.setGravity(new Vector2(0, -3));
                else land();
                break;
            case ADASH:
                world.setGravity(new Vector2(0, -Constants.G));
                dashing = false;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MFLAG.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MFLAG.LEFT;
                break;
            case GDASH:
                world.setGravity(new Vector2(0, -Constants.G));
                dashing = false;
                timer.start(1, NFLAG.DASH_COOLDOWN, this);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = MFLAG.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = MFLAG.LEFT;
                break;
            case DASH_COOLDOWN:
                dashConsumed = false;
                break;
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isFalling() {
        return b2body.getLinearVelocity().y < 0;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setWallState(int wallState) { this.wallState = wallState; }

    public int getWallState() {
        return wallState;
    }

    public void setMovementState(Constants.MFLAG direction) { this.movementState = direction; }

    public boolean isWallGrabbed() {
        return wallGrabbed;
    }

    public MFLAG getMovementState() { return movementState; }

    public boolean isGlideConsumed() { return glideConsumed; }

    public boolean isDashConsumed() { return dashConsumed; }
}
