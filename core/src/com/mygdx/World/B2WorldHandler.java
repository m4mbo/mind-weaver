package com.mygdx.World;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.Door;
import com.mygdx.Objects.Lever;
import com.mygdx.Objects.Platform;
import com.mygdx.Objects.PressurePlate;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.BaseGoblin;
import com.mygdx.RoleCast.Mage;
import com.mygdx.RoleCast.Pet;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.UtilityStation;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class B2WorldHandler {

    private final World world;
    private final MyResourceManager resourceManager;
    private final UtilityStation util;

    public B2WorldHandler(World world, TiledMap map, MyResourceManager resourceManager, MyTimer timer, AtomicInteger eidAllocator, UtilityStation util, int level) {

        this.world = world;
        this.resourceManager = resourceManager;
        this.util = util;

        // Creating Mage and pet
        Mage mage = new Mage(250, 140, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
        util.getCharacterCycle().initialize(mage);
        util.getEntityHandler().addEntity(mage);
        util.getEntityHandler().addPet(new Pet(world, 250, 200, eidAllocator.getAndIncrement(), resourceManager, util));


        BodyDef bdef  = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create ground
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.BIT_GROUND;
            body.createFixture(fdef).setUserData("ground");
        }

//        // Create spikes
//        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
//            Rectangle rect = object.getRectangle();
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
//            body = world.createBody(bdef);
//            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
//            fdef.shape = shape;
//            fdef.filter.categoryBits = Constants.BIT_HAZARD;
//            body.createFixture(fdef).setUserData("hazard");
//        }

        // Create see through fixtures
        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.BIT_GROUND;
            body.createFixture(fdef).setUserData("seethrough");
        }

        // Create base goblins
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getEntityHandler().addEntity(new BaseGoblin(rect.getX(), rect.getY(), world, eidAllocator.getAndIncrement(), timer, resourceManager, util));
        }

        createObjects(level);

        util.getVisionMap().initialize(util.getEntityHandler());

    }

    public void createObjects(int level) {

        switch (level) {
            case 1:
                Door door = new Door(world, resourceManager, 567, 151.8f, 1);
                util.getObjectHandler().addObject(door);
                PressurePlate pressurePlate = new PressurePlate(world, resourceManager, 598, 116.5f, 1);
                pressurePlate.addReactable(door);
                util.getObjectHandler().addObject(pressurePlate);
                Lever lever = new Lever(400, 140, world, resourceManager);
                LinkedList<Vector2> positions = new LinkedList<>();
                positions.add(new Vector2(400 / Constants.PPM, 110 / Constants.PPM));
                positions.add(new Vector2(500 / Constants.PPM, 110 / Constants.PPM));
                Platform platform = new Platform(positions, world, resourceManager);
                lever.addReactable(platform);
                util.getObjectHandler().addObject(lever);
                util.getObjectHandler().addObject(platform);
                break;
            case 2:

                break;
            case 3:

                break;
        }


    }
}
