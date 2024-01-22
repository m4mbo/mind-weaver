package com.mygdx.Objects.Enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Handlers.MyTimer;
import com.mygdx.Tools.Constants;

public class SideCrawler extends Enemy {
    private MyTimer timer;
    public SideCrawler(int x, int y, World world, int id, MyResourceManager resourceManager, MyTimer timer) {

        super(x, y, world, id, resourceManager);

        this.timer = timer;

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();

        //Create body fixture
        circleShape.setRadius(8 / Constants.PPM);
        fdef.friction = 0;  // No friction allows for players sliding next to walls
        fdef.shape = circleShape;
        fdef.filter.categoryBits = Constants.BIT_HAZARD;
        b2body.createFixture(fdef).setUserData("side_crawler");

    }
}
