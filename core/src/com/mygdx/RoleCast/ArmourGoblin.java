package com.mygdx.RoleCast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.MyTimer;

public class ArmourGoblin extends PlayableCharacter {

    private CircleShape circleShape;
    private FixtureDef fdef;

    public ArmourGoblin(int x, int y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, CharacterCycle characterCycle, VisionMap visionMap) {
        super(world, id, timer, myResourceManager, characterCycle, visionMap);

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("armour_idle"), 19, 19)[0], 1/5f, false, 1f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        circleShape = new CircleShape();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, 8 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GOBLIN;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_MAGE | Constants.BIT_GOBLIN | Constants.BIT_ROV | Constants.BIT_FEET;
        b2body.createFixture(fdef).setUserData(id);

        fdef = new FixtureDef();

        //Create player hitbox
        polygonShape.setAsBox(9 / Constants.PPM, 9 / Constants.PPM, new Vector2(0, 0), 0);
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
        polygonShape.setAsBox(6.6f / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -8 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_FEET;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_GOBLIN | Constants.BIT_MAGE;
        b2body.createFixture(fdef).setUserData("bottomSensor");

        // Settings for later
        circleShape.setRadius(3 / Constants.PPM);
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_HAZARD;
        fdef.filter.maskBits = Constants.BIT_GOBLIN | Constants.BIT_MAGE;
    }

    @Override
    public void moveRight() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(Constants.MAX_SPEED_X - 0.5f, b2body.getLinearVelocity().y);
    }

    @Override
    public void moveLeft() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(-0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(-Constants.MAX_SPEED_X + 0.5f, b2body.getLinearVelocity().y);
    }

    @Override
    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_run"), 19, 19)[0], 1/10f, false, 1f);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_idle"), 19, 19)[0], 1/5f, false, 1f);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_jump"), 19, 19)[0], 1/17f, true, 1f);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_fall"), 19, 19)[0], 1/5f, true, 1f);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_land"), 19, 19)[0], 1/14f, false, 1f);
                break;
            case ATTACK:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_attack"), 23, 19)[0], 1/20f, true, 1f);
                break;
        }
    }

    @Override
    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 2.5f), b2body.getWorldCenter(), true);
    }

    public void attack() {
        if (isStateActive(Constants.PSTATE.ATTACK_STUN) || isStateActive(Constants.PSTATE.ATTACKING)) return;
        addPlayerState(Constants.PSTATE.ATTACKING);
        timer.start(0.1f, "attack_hb", this);
    }

    public void slash() {
        circleShape.setPosition(facingRight ? new Vector2(9 / Constants.PPM, 0) : new Vector2(-9 / Constants.PPM, 0));
        fdef.shape = circleShape;
        b2body.createFixture(fdef).setUserData("attack_box");
        timer.start(0.2f, "attack_anim", this);
    }

    @Override
    public void notify(String flag) {
        switch (flag) {
            case "stun":
                removePlayerState(Constants.PSTATE.STUNNED);
                if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = Constants.MSTATE.RIGHT;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = Constants.MSTATE.LEFT;
                break;
            case "land":
                removePlayerState(Constants.PSTATE.LANDING);
                break;
            case "stop":
                movementState = Constants.MSTATE.HSTILL;
                break;
            case "attack_anim":
                for (Fixture fixture : b2body.getFixtureList()) {
                    if (fixture.getUserData() instanceof String && fixture.getUserData().equals("attack_box")) b2body.destroyFixture(fixture);
                }
                removePlayerState(Constants.PSTATE.ATTACKING);
                addPlayerState(Constants.PSTATE.ATTACK_STUN);
                timer.start(0.2f, "attack", this);
                break;
            case "attack" :
                removePlayerState(Constants.PSTATE.ATTACK_STUN);
                break;
            case "attack_hb":
                slash();
                break;
        }
    }
}
