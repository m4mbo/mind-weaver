package com.mygdx.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Interaction.MyTimer;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.ShapeDrawer;

public class BaseGoblin extends PlayableCharacter{

    public BaseGoblin(int x, int y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, ShapeDrawer shapeDrawer, EntityHandler entityHandler) {
        super(world, id, timer, myResourceManager, shapeDrawer, entityHandler);

        loadSprites();

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_idle"), 32, 32)[0], 1/5f, false, 1.25f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, 15 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GOBLIN;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_MAGE | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData(id);

        fdef = new FixtureDef();

        //Create player hitbox
        polygonShape.setAsBox(9 / Constants.PPM, 16 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.filter.maskBits = Constants.BIT_HAZARD;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("goblin_hb");

        //Create mage range of vision
        circleShape.setRadius(140 / Constants.PPM);
        fdef.shape = circleShape;
        fdef.isSensor = true;
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
        polygonShape.setAsBox(6.6f / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -17 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("bottomSensor");
    }

    @Override
    public void loadSprites() {
        // Loading all textures
        resourceManager.loadTexture("player_run.png", "goblin_run");
        resourceManager.loadTexture("player_idle.png", "goblin_idle");
        resourceManager.loadTexture("player_jump.png", "goblin_jump");
        resourceManager.loadTexture("player_land.png", "goblin_land");
        resourceManager.loadTexture("player_fall.png", "goblin_fall");
    }

    public void update(float delta) {
        // Capping y velocity
        if (b2body.getLinearVelocity().y < -Constants.MAX_SPEED_Y)
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -Constants.MAX_SPEED_Y));

        // Animation priority
        if (isFalling()) {
            currAState = Constants.ASTATE.FALL;
            b2body.setLinearDamping(0);
        } else if (!isStateActive(Constants.PSTATE.ON_GROUND)) {
            currAState = Constants.ASTATE.JUMP;
        }

        if (isStateActive(Constants.PSTATE.STUNNED)) movementState = Constants.MSTATE.PREV;

        switch (movementState) {
            case LEFT:
                if (isStateActive(Constants.PSTATE.ON_GROUND) && !isStateActive(Constants.PSTATE.LANDING)) currAState = Constants.ASTATE.RUN;
                facingRight = false;
                moveLeft();
                break;
            case RIGHT:
                if (isStateActive(Constants.PSTATE.ON_GROUND) && !isStateActive(Constants.PSTATE.LANDING)) currAState = Constants.ASTATE.RUN;
                facingRight = true;
                moveRight();
                break;
            case PREV:
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y);
                break;
            case HSTILL:
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
                if (!isStateActive(Constants.PSTATE.ON_GROUND) || isStateActive(Constants.PSTATE.LANDING)) break;
                else currAState = Constants.ASTATE.IDLE;
                break;
            case FSTILL:
                b2body.setLinearVelocity(0, 0);
                break;
        }

        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }
        // Update the animation
        animation.update(delta);
    }

    @Override
    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_run"), 32, 32)[0], 1/14f, false, 1.25f);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_idle"), 32, 32)[0], 1/5f, false, 1.25f);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_jump"), 32, 32)[0], 1/17f, true, 1.25f);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_fall"), 32, 32)[0], 1/5f, true, 1.25f);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("goblin_land"), 32, 32)[0], 1/14f, false, 1.25f);
                break;
        }
    }

    @Override
    public void die() { }
}