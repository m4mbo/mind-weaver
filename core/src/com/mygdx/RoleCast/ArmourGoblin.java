package com.mygdx.RoleCast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Tools.UtilityStation;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.EnemyController;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.MyTimer;

public class ArmourGoblin extends PlayableCharacter {

    private final CircleShape circleShape;
    private FixtureDef fdef;

    public ArmourGoblin(float x, float y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, UtilityStation utilityStation) {

        super(world, id, timer, myResourceManager, utilityStation);

        enemyController = new EnemyController(util.getCharacterCycle().getCurrentCharacter(), this, util.getVisionMap(), timer);

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
        polygonShape.setAsBox(7 / Constants.PPM, 8 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.friction = 0;
        fdef.filter.categoryBits = Constants.BIT_GOBLIN;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_MAGE | Constants.BIT_GOBLIN | Constants.BIT_ROV | Constants.BIT_FEET | Constants.BIT_HAZARD | Constants.BIT_INTERACT;
        b2body.createFixture(fdef).setUserData(id);

        fdef = new FixtureDef();

        //Create player hitbox
        polygonShape.setAsBox(5 / Constants.PPM, 9 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.filter.maskBits = Constants.BIT_HAZARD;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("goblin_hb");

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
        polygonShape.setAsBox(4 / Constants.PPM, 1 / Constants.PPM, new Vector2(0, -8 / Constants.PPM), 0);
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
        else b2body.setLinearVelocity(Constants.MAX_SPEED_X - 0.9f, b2body.getLinearVelocity().y);
    }

    @Override
    public void moveLeft() {
        //Initial acceleration
        if (b2body.getLinearVelocity().x == 0) b2body.applyLinearImpulse(new Vector2(-0.5f, 0), b2body.getWorldCenter(), true);
        else b2body.setLinearVelocity(-Constants.MAX_SPEED_X + 0.9f, b2body.getLinearVelocity().y);
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
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_attack"), 24, 19)[0], 1/31f, true, 1f);
                break;
            case DEATH:
                setAnimation(TextureRegion.split(resourceManager.getTexture("armour_death"), 23, 20)[0], 1/13f, true, 1f);
                break;
        }
    }

    @Override
    public void jump() {
        util.getParticleHandler().addParticleEffect("dust_ground", facingRight ? b2body.getPosition().x - 5 / Constants.PPM : b2body.getPosition().x - 3 / Constants.PPM, b2body.getPosition().y - 10/Constants.PPM);
        b2body.applyLinearImpulse(new Vector2(0, 2.5f), b2body.getWorldCenter(), true);
    }

    public void attack() {
        if (isStateActive(Constants.PSTATE.ATTACK_STUN) || isStateActive(Constants.PSTATE.ATTACKING)) return;
        resourceManager.getSound("attack").play(0.2f);
        util.getParticleHandler().addParticleEffect(facingRight ? "air_right" : "air_left", b2body.getPosition().x, b2body.getPosition().y - 4 / Constants.PPM);
        addPlayerState(Constants.PSTATE.ATTACKING);
        timer.start(0.1f, "attack_hb", this);
    }

    public void slash() {
        circleShape.setPosition(facingRight ? new Vector2(9 / Constants.PPM, 0) : new Vector2(-9 / Constants.PPM, 0));
        fdef.shape = circleShape;
        b2body.createFixture(fdef).setUserData("attack");
        timer.start(0.1f, "attack_anim", this);
    }

    @Override
    public void notify(String flag) {
        switch (flag) {
            case "stun":
                removePlayerState(Constants.PSTATE.STUNNED);
                if (util.getCharacterCycle().getCurrentCharacter().equals(this)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.D)) movementState = Constants.MSTATE.RIGHT;
                    else if (Gdx.input.isKeyPressed(Input.Keys.A)) movementState = Constants.MSTATE.LEFT;
                    else if (isStateActive(Constants.PSTATE.ON_GROUND)) movementState = Constants.MSTATE.HSTILL;
                } else {
                    movementState = Constants.MSTATE.HSTILL;
                }
                break;
            case "land":
                removePlayerState(Constants.PSTATE.LANDING);
                break;
            case "stop":
                movementState = Constants.MSTATE.HSTILL;
                break;
            case "attack_anim":
                for (Fixture fixture : b2body.getFixtureList()) {
                    if (fixture.getUserData() instanceof String && fixture.getUserData().equals("attack")) b2body.destroyFixture(fixture);
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
            case "hit" :
                removePlayerState(Constants.PSTATE.HIT);
                break;
            case "death_and_disposal":
                dispose();
                util.getEntityHandler().addEntityOperation(this, "die");
                break;
            case "death":
                util.getEntityHandler().addEntityOperation(this, "die");
                break;
        }
    }
}
