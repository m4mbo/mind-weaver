package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Graphics.ParticleHandler;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Constants;

public class BaseGoblin extends PlayableCharacter{

    public BaseGoblin(float x, float y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, CharacterCycle characterCycle, VisionMap visionMap, ParticleHandler particleHandler) {
        super(world, id, timer, myResourceManager, characterCycle, visionMap, particleHandler);

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_idle"), 20, 14)[0], 1/5f, false, 1f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, 6 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GOBLIN;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_MAGE | Constants.BIT_GOBLIN | Constants.BIT_ROV | Constants.BIT_FEET | Constants.BIT_HAZARD;
        b2body.createFixture(fdef).setUserData(id);

        fdef = new FixtureDef();

        //Create player hitbox
        polygonShape.setAsBox(9 / Constants.PPM, 7 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.filter.maskBits = Constants.BIT_HAZARD;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("goblin_hb");

        //Create mage range of vision
        circleShape.setRadius(140 / Constants.PPM);
        fdef.shape = circleShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ROV;
        fdef.filter.maskBits = Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("vision");

        //Create right sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(8.2f / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("rightSensor");

        //Create left sensor
        polygonShape.setAsBox(1 / Constants.PPM, 3 / Constants.PPM, new Vector2(-8.2f / Constants.PPM, 0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("leftSensor");

        //Create bottom sensor
        polygonShape.setAsBox(6.6f / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -6 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_FEET;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_GOBLIN | Constants.BIT_MAGE;
        b2body.createFixture(fdef).setUserData("bottomSensor");
    }

    @Override
    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_run"), 20, 14)[0], 1/14f, false, 1f);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_idle"), 20, 14)[0], 1/5f, false, 1f);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_jump"), 20, 14)[0], 1/17f, true, 1f);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_fall"), 20, 14)[0], 1/5f, true, 1f);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_land"), 20, 14)[0], 1/14f, false, 1f);
                break;
        }
    }

    public void land() {
        addPlayerState(Constants.PSTATE.ON_GROUND);
        addPlayerState(Constants.PSTATE.LANDING);
        if (airIterations >= 5) {
            particleHandler.addParticleEffect("dust_ground", facingRight ? b2body.getPosition().x - 4 / Constants.PPM : b2body.getPosition().x - 2 / Constants.PPM, b2body.getPosition().y - 7.5f / Constants.PPM);
            currAState = Constants.ASTATE.LAND;
        }
        timer.start(0.2f, "land", this);
        world.setGravity(new Vector2(0, -Constants.G));
        b2body.setLinearDamping(0);
    }

    public void jump() {
        particleHandler.addParticleEffect("dust_ground", facingRight ? b2body.getPosition().x - 4 / Constants.PPM : b2body.getPosition().x - 2 / Constants.PPM, b2body.getPosition().y - 7.5f / Constants.PPM);
        b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
    }
}
