package com.mygdx.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Handlers.MyTimer;
import com.mygdx.Objects.Enemies.SideCrawler;

import java.util.concurrent.atomic.AtomicInteger;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map, MyResourceManager resourceManager, MyTimer timer, AtomicInteger eidAllocator) {

        BodyDef bdef  = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create ground
        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.BIT_GROUND;
            body.createFixture(fdef).setUserData("floor");
        }

        // Create spikes
        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.BIT_HAZARD;
            body.createFixture(fdef).setUserData("hazard");
        }

        // Create map camera sections (where the camera will move to)
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Constants.BIT_CAMERA;
            body.createFixture(fdef).setUserData("camera_section");
        }

        // Create map camera sections (where the camera will move to)
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Constants.BIT_CHECKPOINT;
            body.createFixture(fdef).setUserData("checkpoint");
        }

        // Create side_crawlers
        for (RectangleMapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new SideCrawler((int) rect.getX(), (int) rect.getY(), world, eidAllocator.getAndIncrement(), resourceManager, timer);
        }
    }

}
