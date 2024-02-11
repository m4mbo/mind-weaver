package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class Merchant extends Entity{

    public Merchant(float x, float y, int id, World world, MyResourceManager resourceManager) {
        super(id, resourceManager);

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("merchant_idle"), 16, 14)[0], 1/5f, false, 1f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(7 / Constants.PPM, 6 / Constants.PPM, new Vector2(0, 0), 0);
        fdef.shape = polygonShape;
        fdef.density = 1000;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData(id);
    }

}
