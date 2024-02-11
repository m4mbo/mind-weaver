package com.mygdx.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.*;
import com.mygdx.RoleCast.*;
import com.mygdx.Scenes.HUD;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.UtilityStation;
import java.util.concurrent.atomic.AtomicInteger;

public class B2WorldHandler {

    private final World world;
    private final MyResourceManager resourceManager;
    private final UtilityStation util;

    public B2WorldHandler(World world, TiledMap map, MyResourceManager resourceManager, MyTimer timer, AtomicInteger eidAllocator, UtilityStation util, int level, HUD hud) {

        this.world = world;
        this.resourceManager = resourceManager;
        this.util = util;

        createPlayer(eidAllocator, timer, hud, level);

        if (level == 2) util.getEntityHandler().addEntity(new Merchant(460, 212, eidAllocator.getAndIncrement(), world, resourceManager));

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

        // Create ending sensors
        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getLightManager().addConeLight(rect.getX() / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM, 60, new Color(96 / 255f, 130 / 255f, 182 / 255f, 1), 180, 90l);
        }

        // Create spikes
        for (RectangleMapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Constants.BIT_HAZARD;
            body.createFixture(fdef).setUserData("spike");
        }

        // Create see through fixtures
        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = false;
            fdef.filter.categoryBits = Constants.BIT_GROUND;
            body.createFixture(fdef).setUserData("seethrough");
        }

        // Create base goblins
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getEntityHandler().addEntity(new BaseGoblin(rect.getX(), rect.getY(), world, eidAllocator.getAndIncrement(), timer, resourceManager, util));
        }

        // Create armour goblins
        for (RectangleMapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getEntityHandler().addEntity(new ArmourGoblin(rect.getX(), rect.getY(), world, eidAllocator.getAndIncrement(), timer, resourceManager, util));
        }

        // Create armour goblins
        for (RectangleMapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getObjectHandler().addObject(new Item(rect.getX(), rect.getY(), world, "papaya", util.getShaderHandler(), resourceManager));
        }

        createObjects(level);

        util.getVisionMap().initialize(util.getEntityHandler(), util.getCharacterCycle());

    }

    public void createObjects(int level) {
        Door door1;
        Door door2;
        PressurePlate pressurePlate;
        switch (level) {
            case 1:
                door1 = new Door(world, resourceManager, 567, 151.8f, 1, false);
                util.getObjectHandler().addObject(door1);
                pressurePlate = new PressurePlate(world, resourceManager, 598, 116.5f, 1);
                pressurePlate.addReactable(door1);
                util.getObjectHandler().addObject(pressurePlate);
                break;
            case 2:
                door1 = new Door(world, resourceManager, 819, 278f, 2, false);
                util.getObjectHandler().addObject(door1);
                pressurePlate = new PressurePlate(world, resourceManager, 749, 298.5f, 2);
                pressurePlate.addReactable(door1);
                util.getObjectHandler().addObject(pressurePlate);
                break;
            case 3:

                door1 = new Door(world, resourceManager, 1511, 236f, 1, true);
                util.getObjectHandler().addObject(door1);
                door2 = new Door(world, resourceManager, 1630, 320f, 1, false);
                util.getObjectHandler().addObject(door2);

                pressurePlate = new PressurePlate(world, resourceManager, 1626, 256.5f, 1);
                pressurePlate.addReactable(door1);
                pressurePlate.addReactable(door2);
                util.getObjectHandler().addObject(pressurePlate);


//                Lever lever = new Lever(400, 140, world, resourceManager);
//                LinkedList<Vector2> positions = new LinkedList<>();
//                positions.add(new Vector2(400 / Constants.PPM, 110 / Constants.PPM));
//                positions.add(new Vector2(500 / Constants.PPM, 110 / Constants.PPM));
//                Platform platform = new Platform(positions, world, resourceManager);
//                lever.addReactable(platform);
//                util.getObjectHandler().addObject(lever);
//                util.getObjectHandler().addObject(platform);
                break;
        }
    }

    public void createPlayer(AtomicInteger eidAllocator, MyTimer timer, HUD hud, int level) {

        Mage mage = null;

        switch (level) {
            case 1:
                mage = new Mage(250, 140, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
                util.getEntityHandler().addPet(new Pet(world, 250, 300, eidAllocator.getAndIncrement(), resourceManager, util));
                break;
            case 2:
                mage = new Mage(437, 212, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
                util.getEntityHandler().addPet(new Pet(world, 437, 212, eidAllocator.getAndIncrement(), resourceManager, util));
                break;
            case 3:
                mage = new Mage(1078, 359, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
                util.getEntityHandler().addPet(new Pet(world, 1078, 359, eidAllocator.getAndIncrement(), resourceManager, util));
                break;
            default:
                break;

        }
        hud.setPlayer(mage);
        util.getCharacterCycle().initialize(mage);
        util.getEntityHandler().addEntity(mage);
    }
}
