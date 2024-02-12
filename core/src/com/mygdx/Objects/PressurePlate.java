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
    private int level;
    private int pressedIterations;
    private int closedIterations;
    private boolean isPressed;
    private float currHeight;
    private float height;
    private PolygonShape polygonShape;
    private FixtureDef fdef;

    public PressurePlate(World world, MyResourceManager resourceManager, float x, float y, int level) {

        super(world, resourceManager);

        this.level = level;

        pressedIterations = 0;
        closedIterations = 0;
        height = 3;
        currHeight = height;

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

        fdef = new FixtureDef();
        polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(7 / Constants.PPM, height / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.density = 0;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_SUPPORT | Constants.BIT_FEET | Constants.BIT_MAGE | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("plate");

        polygonShape.setAsBox(0.2f / Constants.PPM, 20 / Constants.PPM, new Vector2(-9f / Constants.PPM, -20 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_SUPPORT;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body2.createFixture(fdef).setUserData("support");

        polygonShape.setAsBox(0.1f / Constants.PPM, 20 / Constants.PPM, new Vector2(9f / Constants.PPM, -20 / Constants.PPM), 0);
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

        if (level == 1) threshold = -0.006f;
        else if (level == 2) threshold = -0.009f;
        else threshold = -0.006f;

        currAState = Constants.ASTATE.CLOSED;
        prevAState = Constants.ASTATE.CLOSED;

        setAnimation(TextureRegion.split(resourceManager.getTexture(level == 1 ? "pressureplate_up" : "pressureplate_up2"), 14, 5)[0], 1/14f, true, 1f, animation.getCurrentFrame());

        // Assigning values for later use
        fdef.density = 0;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_SUPPORT | Constants.BIT_FEET | Constants.BIT_MAGE | Constants.BIT_GOBLIN;
    }

    public void update(float delta) {

        if (distanceJoint.getReactionForce(delta).y < threshold) pressedIterations++;
        else { closedIterations++; }

        if (pressedIterations >= 10) {
            pressedIterations = 0;
            closedIterations = 0;
            currAState = Constants.ASTATE.OPEN;
            isPressed = true;
        }

        if (closedIterations >= 10) {
            closedIterations = 0;
            pressedIterations = 0;
            currAState = Constants.ASTATE.CLOSED;
            isPressed = false;
        }

        step();

        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }

        animation.update(delta);
    }

    public void handleAnimation() {
        interact();
        if (currAState == Constants.ASTATE.OPEN) setAnimation(TextureRegion.split(resourceManager.getTexture(level == 1 ? "pressureplate_down" : "pressureplate_down2"), 14, 5)[0], 1/14f, true, 1f, animation.getFrameNumber() - animation.getCurrentFrame());
        else setAnimation(TextureRegion.split(resourceManager.getTexture(level == 1 ? "pressureplate_up" : "pressureplate_up2"), 14, 5)[0], 1/14f, true, 1f, animation.getFrameNumber() - animation.getCurrentFrame());
    }

    public void step() {

        if (currHeight <= 60f / Constants.PPM && isPressed) return;
        else if (currHeight >= height && !isPressed) return;

        if (isPressed) currHeight -= 20 / Constants.PPM;
        else currHeight += 20 / Constants.PPM;

        // Destroying current fixture
        Fixture fixture = b2body.getFixtureList().get(0);
        b2body.destroyFixture(fixture);

        // Creating new fixture based on current height
        polygonShape.setAsBox(8 / Constants.PPM, currHeight / Constants.PPM, new Vector2(0, (currHeight - height) / Constants.PPM), 0);
        fdef.shape = polygonShape;
        b2body.createFixture(fdef).setUserData("plate");
    }

}
