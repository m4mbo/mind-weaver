package com.mygdx.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.*;
import com.mygdx.RoleCast.*;
import com.mygdx.Scenes.HUD;
import com.mygdx.Tools.MyTimer;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.TextureDrawer;
import com.mygdx.Tools.UtilityStation;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class B2WorldHandler {

    private final World world;
    private final MyResourceManager resourceManager;
    private final UtilityStation util;

    public B2WorldHandler(World world, TiledMap map, MyResourceManager resourceManager, MyTimer timer, AtomicInteger eidAllocator, UtilityStation util, int level, HUD hud, TextureDrawer textureDrawer) {

        this.world = world;
        this.resourceManager = resourceManager;
        this.util = util;

        createPlayer(eidAllocator, timer, hud, level);

        BodyDef bdef  = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        if (level == 1) hud.pushCutscene("intro");

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
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Constants.BIT_GROUND;
            fdef.filter.maskBits = Constants.BIT_MAGE;
            body.createFixture(fdef).setUserData("end");
        }

        fdef = new FixtureDef();

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

        // Create merchant
        for (RectangleMapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getEntityHandler().addEntity(new Merchant(rect.getX(), rect.getY(), eidAllocator.getAndIncrement(), world, resourceManager, hud));
        }

        // Create bug
        for (RectangleMapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            util.getObjectHandler().addObject(new Item(rect.getX(), rect.getY(), world, "bug", util.getShaderHandler(), resourceManager));
        }

        // Create messages
        for (RectangleMapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            textureDrawer.addTexture(resourceManager.getTexture("x"), rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, 0.6f);
        }
        for (RectangleMapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            textureDrawer.addTexture(resourceManager.getTexture("shift"), rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, 0.6f);
        }
        for (RectangleMapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            textureDrawer.addTexture(resourceManager.getTexture("space"), rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, 0.6f);
        }

        createObjects(level);

        util.getVisionMap().initialize(util.getEntityHandler(), util.getCharacterCycle());

    }

    public void createObjects(int level) {

        PressurePlate pressurePlate;
        LinkedList<Vector2> positions;

        switch (level) {
            case 1:
                createDoorAndPressurePlate(567, 151.8f, 598, 116.5f, 1, false);
                break;
            case 2:
                createDoorAndPressurePlate(819, 278f, 749, 298.5f, 2, false);
                break;
            case 3:
                pressurePlate = createDoorAndPressurePlate(1511, 250f, 1626, 256.5f, 1, true);
                addDoor(1630, 320f, pressurePlate, 1, false);
            case 4:

                createDoorAndPressurePlate(469, 222, 469, 298.5f, 1, false);
                createDoorAndPressurePlate(540, 291, 620, 409.5f, 1, false);
                createDoorAndPressurePlate(540, 348, 620, 368.5f, 1, false);
                createDoorAndPressurePlate(259, 348, 70, 270.5f, 1, false);
                createDoorAndPressurePlate(175, 278, 130, 270.5f, 1, false);
                createDoorAndPressurePlate(301, 446, 382, 466.5f, 1, false);

                positions = new LinkedList<>();
                positions.add(new Vector2(321 / Constants.PPM, 205 / Constants.PPM));
                positions.add(new Vector2(321 / Constants.PPM, 329 / Constants.PPM));
                createLeverAndPlatform(367.5f, 140, true, positions);

                positions = new LinkedList<>();
                positions.add(new Vector2(504 / Constants.PPM, 161 / Constants.PPM));
                positions.add(new Vector2(504 / Constants.PPM, 329 / Constants.PPM));
                createLeverAndPlatform(543, 175, false, positions);

                positions = new LinkedList<>();
                positions.add(new Vector2(198 / Constants.PPM, 259 / Constants.PPM));
                positions.add(new Vector2(260 / Constants.PPM, 259 / Constants.PPM));
                createLeverAndPlatform(346.5f, 344, false, positions);
                break;
            case 5:

                createDoorAndPressurePlate(343, 390, 600, 480.5f, 2, false);
                createDoorAndPressurePlate(427, 390, 496, 396.5f, 1, false);
                pressurePlate = createDoorAndPressurePlate(623, 390, 620, 480.5f, 1, false);
                addDoor(566, 418, pressurePlate, 1, true);
                createDoorAndPressurePlate(819, 390, 675, 410.5f, 1, false);
                createDoorAndPressurePlate(875, 390, 695, 368.5f, 1, false);

                //Custom platforms

                positions = new LinkedList<>();
                positions.add(new Vector2(322 / Constants.PPM, 350 / Constants.PPM));
                positions.add(new Vector2(322 / Constants.PPM, 441 / Constants.PPM));
                positions.add(new Vector2(485 / Constants.PPM, 441 / Constants.PPM));
                positions.add(new Vector2(322 / Constants.PPM, 441 / Constants.PPM));
                Platform platform1 = new Platform(new LinkedList<>(positions), world, resourceManager);

                positions = new LinkedList<>();
                positions.add(new Vector2(322 / Constants.PPM, 335 / Constants.PPM));
                positions.add(new Vector2(448 / Constants.PPM, 335 / Constants.PPM));
                positions.add(new Vector2(448 / Constants.PPM, 441 / Constants.PPM));
                positions.add(new Vector2(448 / Constants.PPM, 335 / Constants.PPM));
                Platform platform2 = new Platform(new LinkedList<>(positions), world, resourceManager);

                pressurePlate.addReactable(platform2);

                Lever lever1 = new Lever(367.5f, 370f, world, resourceManager, true);
                Lever lever2 = new Lever(402.5f, 370f, world, resourceManager, false);

                lever1.addReactable(platform1);
                lever1.addReactable(platform2);
                lever2.addReactable(platform2);
                util.getObjectHandler().addObject(lever1);
                util.getObjectHandler().addObject(lever2);
                util.getObjectHandler().addObject(platform1);
                util.getObjectHandler().addObject(platform2);

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
            case 4:
                mage = new Mage(437, 359, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
                util.getEntityHandler().addPet(new Pet(world, 1078, 359, eidAllocator.getAndIncrement(), resourceManager, util));
                break;
            case 5:
                mage = new Mage(385, 374, world, eidAllocator.getAndIncrement(), timer, resourceManager, util);
                util.getEntityHandler().addPet(new Pet(world, 470, 340, eidAllocator.getAndIncrement(), resourceManager, util));
                break;
            default:
                break;

        }
        hud.setPlayer(mage);
        util.getCharacterCycle().initialize(mage);
        util.getEntityHandler().addEntity(mage);
    }

    public PressurePlate createDoorAndPressurePlate(float xDoor, float yDoor, float xPressurePlate, float yPressurePlate, int level, boolean isDoorOpen) {
        Door door = new Door(world, resourceManager, xDoor, yDoor, level, isDoorOpen);
        util.getObjectHandler().addObject(door);
        PressurePlate pressurePlate = new PressurePlate(world, resourceManager, xPressurePlate, yPressurePlate, level);
        pressurePlate.addReactable(door);
        util.getObjectHandler().addObject(pressurePlate);
        return pressurePlate;
    }

    public void addDoor(float xDoor, float yDoor, PressurePlate pressurePlate, int level, boolean isDoorOpen) {
        Door door = new Door(world, resourceManager, xDoor, yDoor, level, isDoorOpen);
        util.getObjectHandler().addObject(door);
        pressurePlate.addReactable(door);
    }

    public void createLeverAndPlatform(float xLever, float yLever, boolean right, LinkedList<Vector2> platformPositions) {
        Lever lever = new Lever(xLever, yLever, world, resourceManager, right);
        Platform platform = new Platform(new LinkedList<>(platformPositions), world, resourceManager);
        lever.addReactable(platform);
        util.getObjectHandler().addObject(lever);
        util.getObjectHandler().addObject(platform);
    }
}
