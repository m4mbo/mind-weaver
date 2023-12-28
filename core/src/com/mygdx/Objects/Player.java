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
        
    }

    public void moveLeft() {

    }

    public void attack() {

    }

    public void glide() {

    }

    public void grab() {

    }
}
