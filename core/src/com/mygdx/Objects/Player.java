package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Helpers.Constants;

public class Player extends Sprite {
    private World world;
    private Body b2body;
    private boolean soar;
    public Player(int x, int y, World world) {
        soar = false;
        this.world = world;
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / Constants.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        b2body.setLinearDamping(5);
    }

    public void moveRight() {
        b2body.applyLinearImpulse(new Vector2(Constants.MAX_SPEED, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft() {
        b2body.applyLinearImpulse(new Vector2(-Constants.MAX_SPEED, 0), b2body.getWorldCenter(), true);
    }

    public void reset() {
        if (b2body.getLinearVelocity().y == 0) soar = false;
        world.setGravity(new Vector2(0, -10));
        b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
    }

    public void attack() {

    }

    public void glide() {
        if (!soar) {
            b2body.applyLinearImpulse(new Vector2((float) Math.pow(b2body.getLinearVelocity().x, 2), 0), b2body.getWorldCenter(), true);
            b2body.applyLinearImpulse(new Vector2(0, (float) Math.pow(b2body.getLinearVelocity().y, 2)), b2body.getWorldCenter(), true);
        }
        soar = true;
        world.setGravity(new Vector2(0, -4));
    }

    public void grab() {

    }

    public boolean movingRight() {
        return !(b2body.getLinearVelocity().x <= Constants.MAX_SPEED);
    }

    public boolean movingLeft() {
        return !(b2body.getLinearVelocity().x >= -Constants.MAX_SPEED);
    }

    public boolean aloft() {
        return b2body.getLinearVelocity().y != 0;
    }

    public boolean falling() {
        return b2body.getLinearVelocity().y < 0;
    }
}
