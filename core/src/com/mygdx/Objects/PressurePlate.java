package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class PressurePlate extends Interactable {
    private Body b2body2;
    private DistanceJoint distanceJoint;
    private float threshold;
    private int iterations;

    public PressurePlate(World world, MyResourceManager resourceManager, float x, float y, int strength) {

        super(world, resourceManager);

        iterations = 0;

        // Creating two bodies for spring joint
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, (y - 30) / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body2 = world.createBody(bdef);

        // Creating the fixtures

        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, 1f / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_SUPPORT | Constants.BIT_FEET | Constants.BIT_MAGE | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("plate");

        polygonShape.setAsBox(0.2f / Constants.PPM, 20 / Constants.PPM, new Vector2(-10f / Constants.PPM, -20 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_SUPPORT;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body2.createFixture(fdef).setUserData("support");

        polygonShape.setAsBox(0.1f / Constants.PPM, 20 / Constants.PPM, new Vector2(10f / Constants.PPM, -20 / Constants.PPM), 0);
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

        djd.length = b2body2.getPosition().y - b2body.getPosition().y;
        djd.collideConnected = true;

        // Max stiffness
        djd.frequencyHz = 0f;
        djd.dampingRatio = 1f;

        distanceJoint = (DistanceJoint) world.createJoint(djd);

        if (strength == 1) threshold = -0.006f;
        else if (strength == 2) threshold = -0.009f;
        else threshold = -0.006f;

        currAState = Constants.ASTATE.CLOSED;
        prevAState = Constants.ASTATE.CLOSED;

        setAnimation(TextureRegion.split(resourceManager.getTexture("pressureplate_up"), 17, 7)[0], 1/10f, true, 1f);
    }

    public void update(float delta) {

        if (distanceJoint.getReactionForce(delta).y < threshold) iterations++;
        else currAState = Constants.ASTATE.CLOSED;

        if (iterations >= 30) {
            iterations = 0;
            currAState = Constants.ASTATE.OPEN;
        }



        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }

        animation.update(delta);
    }

    public void handleAnimation() {
        interact();
        if (currAState == Constants.ASTATE.OPEN) setAnimation(TextureRegion.split(resourceManager.getTexture("pressureplate_down"), 17, 7)[0], 1/19f, true, 1f);
        else setAnimation(TextureRegion.split(resourceManager.getTexture("pressureplate_up"), 17, 7)[0], 1/10f, true, 1f);
    }

}
