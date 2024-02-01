package com.mygdx.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.mygdx.Helpers.Constants;

public class PressurePlate extends Interactable {
    private Body b2body2;

    public PressurePlate(World world, float x, float y, float strength) {

        super(world);

        // Creating two bodies for spring joint
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, (y + 2) / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        bdef.position.set(x / Constants.PPM, (y - 12) / Constants.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body2 = world.createBody(bdef);

        // Creating the fixtures

        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, 1f / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.density = 1 / ((8 / Constants.PPM) * (0.2f / Constants.PPM));
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_SUPPORT | Constants.BIT_FEET | Constants.BIT_MAGE | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("plate");

        polygonShape.setAsBox(0.2f / Constants.PPM, 12 / Constants.PPM, new Vector2(-10f / Constants.PPM, 10 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_SUPPORT;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body2.createFixture(fdef).setUserData("support");

        polygonShape.setAsBox(0.1f / Constants.PPM, 12 / Constants.PPM, new Vector2(10f / Constants.PPM, 10 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_SUPPORT;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body2.createFixture(fdef).setUserData("support");

        polygonShape.setAsBox(8 / Constants.PPM, 0.2f / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        b2body2.createFixture(fdef).setUserData("base");

        DistanceJointDef djd = new DistanceJointDef();

        djd.bodyA = b2body;
        djd.bodyB = b2body2;

        djd.length = 0.2f;
        djd.collideConnected = true;

        djd.frequencyHz = 2f;
        djd.dampingRatio = 10f / Constants.PPM;

        world.createJoint(djd);
    }

}
