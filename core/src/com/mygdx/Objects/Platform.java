package com.mygdx.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.Entity;
import com.mygdx.Tools.MathWizard;
import com.mygdx.Tools.MyResourceManager;
import java.util.LinkedList;

public class Platform extends Reactable {

    private LinkedList<Vector2> positionCycle;

    private LinkedList<Entity> entitiesOnTop;

    private int currPosition;

    public Platform(LinkedList<Vector2> positions, World world, MyResourceManager resourceManager) {
        super(world, resourceManager);
        this.positionCycle = positions;

        currPosition = 0;
        entitiesOnTop = new LinkedList<>();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bdef.position.set(positionCycle.get(currPosition).x, positionCycle.get(currPosition).y);
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);
        b2body.setLinearDamping(5);

        polygonShape.setAsBox(28 / Constants.PPM, 14 / Constants.PPM);
        fdef.shape = polygonShape;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        b2body.createFixture(fdef).setUserData("platform");

        polygonShape.setAsBox(28 / Constants.PPM, 2 / Constants.PPM, new Vector2(0, 14 / Constants.PPM), 0);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_FEET;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float delta) {
        assess();
        animation.update(delta);
    }

    public void react() {
        currPosition++;
        if (currPosition >= positionCycle.size()) currPosition = 0;
    }

    public void assess() {
        Vector2 position = positionCycle.get(currPosition);
        if (MathWizard.inRange(position, b2body.getPosition(), 10f / Constants.PPM)) {
            b2body.setLinearVelocity(0, 0);
        } else {
            step(position);
        }
    }

    public void step(Vector2 position) {
        Vector2 normalized = MathWizard.normalizedDirection(position, b2body.getPosition());
        b2body.setLinearVelocity(new Vector2(normalized.x * Constants.MAX_SPEED_X / 1.1f, normalized.y * Constants.MAX_SPEED_X / 2));
    }

    public void addEntityOnTop(Entity entity) {
        entitiesOnTop.add(entity);
    }

    public void removeEntityOnTop(Entity entity) {
        entitiesOnTop.remove(entity);
    }
}
