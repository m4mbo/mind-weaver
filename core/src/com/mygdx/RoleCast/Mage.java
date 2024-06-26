package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Tools.UtilityStation;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Constants;

public class Mage extends PlayableCharacter {

    private final Vector2 currCheckPoint;
    private Merchant merchantInRange;

    public Mage(float x, float y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, UtilityStation utilityStation) {

        super(world, id, timer, myResourceManager, utilityStation);

        currCheckPoint = new Vector2(x / Constants.PPM, y / Constants.PPM);

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("mage_idle"), 20, 20)[0], 1/5f, false, 1);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(5f / Constants.PPM, 8 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_MAGE;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_GOBLIN | Constants.BIT_FEET | Constants.BIT_INTERACT | Constants.BIT_HAZARD;
        b2body.createFixture(fdef).setUserData(id);

        //Create mage range of vision
        circleShape.setRadius(155 / Constants.PPM);
        fdef.shape = circleShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ROV;
        fdef.filter.maskBits = Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("vision");

        //Create right sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(5 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("rightSensor");

        //Create left sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(-5 / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("leftSensor");

        //Create bottom sensor
        polygonShape.setAsBox(3.5f / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -8 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_FEET;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("bottomSensor");
    }

    @Override
    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_run"), 20, 20)[0], 1/10f, false, 1f);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_idle"), 20, 20)[0], 1/5f, false, 1f);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_jump"), 20, 20)[0], 1/17f, true, 1f);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_fall"), 20, 20)[0], 1/14f, true, 1f);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_land"), 20, 20)[0], 1/5f, false, 1f);
                break;
            case DEATH:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_death"), 24, 20)[0], 1/13f, true, 1f);
                break;
        }
    }

    public void respawn() {
        lives = 3;
        util.getMusicManager().resume();
        b2body.setTransform(currCheckPoint, b2body.getAngle());
        removePlayerState(Constants.PSTATE.DYING);
        movementState = Constants.MSTATE.HSTILL;
    }

    @Override
    public void die() {
        lives--;
        timer.start(0.05f, "hit", this);    // Adding hit red mask
        addPlayerState(Constants.PSTATE.HIT);
        // When player looses all 3 lives, respawn
        if (lives == 0 && !isStateActive(Constants.PSTATE.DYING)) {
            util.getMusicManager().stop();
            resourceManager.getSound("laugh").play(0.3f);
            timer.start(2f, "death", this);     // Adding death operation in 2 seconds
            addPlayerState(Constants.PSTATE.DYING);
        }
    }

    public void setMerchantInRange(Merchant merchant) {
        merchantInRange = merchant;
    }

    public Merchant getMerchantInRange() {
        return merchantInRange;
    }
}
