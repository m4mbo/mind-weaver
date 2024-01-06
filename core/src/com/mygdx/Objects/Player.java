package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants;

public class Player extends Sprite implements Subscriber {
    private final World world;
    private final Body b2body;
    private boolean onGround;
    private boolean glideConsumed;
    private int wallState;      // -1 for left, 1 for right, 0 for none
    private boolean wallGrabbed;
    private Constants.DIRECTION direction;
    public Player(int x, int y, World world) {
        direction = Constants.DIRECTION.HSTILL;
        onGround = true;

        this.world = world;
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
        switch (direction) {
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
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
        b2body.setLinearVelocity(0, -0.5f);
    }

    public void attack() {

    }

    public void glide() {
        if (!glideConsumed) {
            b2body.applyLinearImpulse(new Vector2((float) Math.pow(b2body.getLinearVelocity().x, 2), 0), b2body.getWorldCenter(), true);
            b2body.applyLinearImpulse(new Vector2(0, (float) Math.pow(b2body.getLinearVelocity().y, 2)), b2body.getWorldCenter(), true);
        }
        glideConsumed = true;
        world.setGravity(new Vector2(0, -4));
    }

    public void grab() {
        wallGrabbed = true;
        world.setGravity(new Vector2(0, 0));
        b2body.setLinearVelocity(0, 0);
    }

    public void letGo() {
        wallGrabbed = false;
        b2body.applyLinearImpulse(new Vector2(0, -0.1f), b2body.getWorldCenter(), true);
        world.setGravity(new Vector2(0, -Constants.G));
    }

    public void notify(Constants.TIMER_FLAG flag) {

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

    public void setDirection(Constants.DIRECTION direction) { this.direction = direction; }

    public boolean isWallGrabbed() {
        return wallGrabbed;
    }

    public Constants.DIRECTION getDirection() { return direction; }
}
