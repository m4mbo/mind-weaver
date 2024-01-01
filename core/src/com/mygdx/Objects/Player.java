package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Helpers.Constants;

public class Player extends Sprite {
    private World world;
    private Body b2body;
    public Player(int x, int y, World world) {
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
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }

    public void moveRight() {
        b2body.setLinearVelocity(1, b2body.getLinearVelocity().y);;
    }

    public void moveLeft() {
        b2body.setLinearVelocity(-1, b2body.getLinearVelocity().y);
    }

    public void stop() {
        b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
    }

    public void attack() {

    }

    public void glide() {

    }

    public void grab() {

    }

    public boolean movingRight() {
        return !(b2body.getLinearVelocity().x <= 2);
    }

    public boolean movingLeft() {
        return !(b2body.getLinearVelocity().x >= -2);
    }


}
