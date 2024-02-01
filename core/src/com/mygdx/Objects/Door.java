package com.mygdx.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Helpers.Constants;

public class Door extends Reactable {

    private boolean open;
    private float height;
    private float currHeight;
    private FixtureDef fdef;
    private PolygonShape polygonShape;

    public Door(World world, int x, int y) {

        super(world);

        height = 16;
        currHeight = height;

        // Creating two bodies for spring joint
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, (y + 2) / Constants.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        polygonShape = new PolygonShape();

        //Create body fixture
        polygonShape.setAsBox(8 / Constants.PPM, height / Constants.PPM, new Vector2(0,0), 0);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("door");

    }

    @Override
    public void update(float delta) {
        if (open) step();
    }

    public void step() {
        if (currHeight <= 0) return;
        Fixture fixture = b2body.getFixtureList().get(0);
        b2body.destroyFixture(fixture);

        polygonShape.setAsBox(8 / Constants.PPM, currHeight / Constants.PPM, new Vector2(currHeight - height, 0), 0);
        fdef.shape = polygonShape;
        b2body.createFixture(fdef).setUserData("door");
    }
}
