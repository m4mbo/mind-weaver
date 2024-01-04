package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Tools.Constants;

public class Player extends Sprite {
    private final World world;
    private final Body b2body;
    private boolean onGround;
    private boolean glideConsumed;
    private Constants.wallType wallState;
    private boolean wallGrabbed;
    public Player(int x, int y, World world) {
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
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        b2body.setLinearDamping(5);
    }

    public void wallJump() {
        if (wallState == Constants.wallType.LEFT) {

        } else {

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

    public void reset() {
        if (onGround) glideConsumed = false;
        wallGrabbed = false;
        world.setGravity(new Vector2(0, -11));
        b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
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
        b2body.setLinearVelocity(0, 0);
    }

    public boolean movingRight() {
        return !(b2body.getLinearVelocity().x <= Constants.MAX_SPEED);
    }

    public boolean movingLeft() {
        return !(b2body.getLinearVelocity().x >= -Constants.MAX_SPEED);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean falling() {
        return b2body.getLinearVelocity().y < 0;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setWallState(int i) {
        if (i == 0) {
            wallState = Constants.wallType.NONE;
        } else if (i == 1) {
            wallState = Constants.wallType.RIGHT;
        } else {
            wallState = Constants.wallType.LEFT;
        }
    }

    public Constants.wallType getWallState() {
        return wallState;
    }

    public boolean isWallGrabbed() {
        return wallGrabbed;
    }
}
