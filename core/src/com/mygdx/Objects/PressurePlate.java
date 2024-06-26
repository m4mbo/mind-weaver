package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class PressurePlate extends Interactable {
    private final DistanceJoint distanceJoint;
    private final float threshold;
    private final int level;

    /*
     * Counting for how many ticks was a pressure plate pressed or not
     * Done to avoid spamming pressing behaviour
     */
    private int openIterations;
    private int closedIterations;
    private boolean isPressed;  // Is the current pressure plate pressed
    private float currHeight;   // Current height of plate fixture
    private final float height;     // Default height of plate fixture
    private final PolygonShape polygonShape;
    private final FixtureDef fdef;

    public PressurePlate(World world, MyResourceManager resourceManager, float x, float y, int level) {

        super(world, resourceManager);

        this.level = level;

        openIterations = 0;
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
        Body b2body2 = world.createBody(bdef);

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

        // Adding supports to keep the plate in place
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

        // Creating base where the plate will be hanging from
        polygonShape.setAsBox(8 / Constants.PPM, 0.2f / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        b2body2.createFixture(fdef).setUserData("base");

        // Creating distance joint between base and plate to measure the forces applied to the plate
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

        /*
         * Testing reaction force from joint
         * this will tell us if n characters are standing on top of plate
         */
        if (distanceJoint.getReactionForce(delta).y < threshold) openIterations++;
        else { closedIterations++; }

        // If pressed for 10 ticks, open
        if (openIterations >= 10) {
            openIterations = 0;
            closedIterations = 0;
            currAState = Constants.ASTATE.OPEN;
            isPressed = true;
        }

        // If not pressed for 10 ticks, close
        if (closedIterations >= 10) {
            closedIterations = 0;
            openIterations = 0;
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
