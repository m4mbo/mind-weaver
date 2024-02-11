package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class Door extends Reactable {

    private boolean open;
    private final float height;
    private float currHeight;
    private final FixtureDef fdef;
    private final PolygonShape polygonShape;
    private final int level;

    public Door(World world, MyResourceManager resourceManager, float x, float y, int level, boolean isOpen) {

        super(world, resourceManager);

        this.level = level;

        height = 14;
        currHeight = height;

        currAState = isOpen ? Constants.ASTATE.OPEN : Constants.ASTATE.CLOSED;
        prevAState = currAState;

        open = isOpen;

        setAnimation(TextureRegion.split(resourceManager.getTexture(isOpen ? (level == 1 ? "door_open" : "door_open2") : (level == 1 ? "door_closed" : "door_closed2")), 13, 28)[0], 1/9f, true, 1f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, (y + 2) / Constants.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(6 / Constants.PPM, height / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("door");

    }

    @Override
    public void update(float delta) {
        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }
        animation.update(delta);
        step();
    }

    public void handleAnimation() {
        if (currAState == Constants.ASTATE.OPEN) setAnimation(TextureRegion.split(resourceManager.getTexture(level == 1 ? "door_open" : "door_open2"), 13, 28)[0], 1/9f, true, 1f, animation.getFrameNumber() - animation.getCurrentFrame());
        else setAnimation(TextureRegion.split(resourceManager.getTexture(level == 1 ? "door_closed" : "door_closed2"), 13, 28)[0], 1/9f, true, 1f, animation.getFrameNumber() - animation.getCurrentFrame());
    }

    public void step() {

        if (currHeight <= 0 && open)  {
            if (!b2body.getFixtureList().isEmpty()) b2body.destroyFixture(b2body.getFixtureList().get(0));
            return;
        }
        else if (currHeight >= height && !open) return;

        if (open) currHeight -= 14 / Constants.PPM;
        else currHeight += 14 / Constants.PPM;

        if (!b2body.getFixtureList().isEmpty()) b2body.destroyFixture(b2body.getFixtureList().get(0));

        // Creating new fixture based on current height
        polygonShape.setAsBox(6 / Constants.PPM, currHeight / Constants.PPM, new Vector2(0, (currHeight - height) / Constants.PPM), 0);
        fdef.shape = polygonShape;
        b2body.createFixture(fdef).setUserData("door");
    }

    @Override
    public void react() {
        open = !open;
        if (open) currAState = Constants.ASTATE.OPEN;
        else currAState = Constants.ASTATE.CLOSED;
    }
}
