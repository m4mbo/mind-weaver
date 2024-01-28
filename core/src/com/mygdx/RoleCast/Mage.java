package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Logic.MyTimer;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.ShapeDrawer;

public class Mage extends PlayableCharacter {

    private Vector2 currCheckPoint;
    private int lives;

    public Mage(int x, int y, World world, int id, MyTimer timer, MyResourceManager myResourceManager, int lives, ShapeDrawer shapeDrawer, EntityHandler entityHandler) {
        super(world, id, timer, myResourceManager, shapeDrawer, entityHandler);

        this.lives = lives;

        currCheckPoint = new Vector2(x / Constants.PPM, y / Constants.PPM);

        loadSprites();

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("mage_idle"), 20, 20)[0], 1/5f, false, 1);

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
        fdef.filter.categoryBits = Constants.BIT_MAGE;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_CHECKPOINT | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData(id);

        //Create player hitbox
        polygonShape.setAsBox(9 / Constants.PPM, 16 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.filter.maskBits = Constants.BIT_HAZARD;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("player_hb");

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
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData("bottomSensor");
    }

    @Override
    public void loadSprites() {
        // Loading all textures
        resourceManager.loadTexture("player_run.png", "player_run");
        resourceManager.loadTexture("Mage/mage_idle.png", "mage_idle");
        resourceManager.loadTexture("player_jump.png", "player_jump");
        resourceManager.loadTexture("player_land.png", "player_land");
        resourceManager.loadTexture("player_fall.png", "player_fall");
    }

    @Override
    public void handleAnimation() {
        switch (currAState) {
            case RUN:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_run"), 32, 32)[0], 1/14f, false, 1.25f);
                break;
            case IDLE:
                setAnimation(TextureRegion.split(resourceManager.getTexture("mage_idle"), 20, 20)[0], 1/5f, false, 1);
                break;
            case JUMP:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_jump"), 32, 32)[0], 1/17f, true, 1.25f);
                break;
            case FALL:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_fall"), 32, 32)[0], 1/5f, true, 1.25f);
                break;
            case LAND:
                setAnimation(TextureRegion.split(resourceManager.getTexture("player_land"), 32, 32)[0], 1/14f, false, 1.25f);
                break;
        }
    }

    public void respawn() {
        b2body.setTransform(currCheckPoint, b2body.getAngle());
    }

    @Override
    public void die() {
        lives--;
        //if (lives == 0) return;     // Handle later
        respawn();
    }

    public void setCheckPoint(Vector2 position) { currCheckPoint = position; }
}
