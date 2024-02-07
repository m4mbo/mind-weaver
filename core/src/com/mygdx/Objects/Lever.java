package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class Lever extends Interactable {

    public Lever(float x, float y, World world, MyResourceManager resourceManager) {
        super(world, resourceManager);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(4 / Constants.PPM, 4 / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_INTERACT;
        fdef.filter.maskBits = Constants.BIT_MAGE | Constants.BIT_GOBLIN;
        b2body.createFixture(fdef).setUserData(this);

        setAnimation(TextureRegion.split(resourceManager.getTexture("lever_up"), 7, 10)[0], 1/5f, true, 1f);

        currAState = Constants.ASTATE.OPEN;
        prevAState = currAState;
    }

    @Override
    public void update(float delta) {
        if (currAState != prevAState) {
            handleAnimation();
            prevAState = currAState;
        }
    }

    @Override
    public void handleAnimation() {
        if (currAState == Constants.ASTATE.OPEN) setAnimation(TextureRegion.split(resourceManager.getTexture("lever_up"), 7, 10)[0], 1/9f, true, 1f);
        else setAnimation(TextureRegion.split(resourceManager.getTexture("lever_down"), 7, 10)[0], 1/9f, true, 1f);
    }

    @Override
    public void interact() {
        if (currAState == Constants.ASTATE.OPEN) currAState = Constants.ASTATE.CLOSED;
        else currAState = Constants.ASTATE.OPEN;
        for (Reactable reactable : reactables) {
            reactable.react();
        }
    }
}
