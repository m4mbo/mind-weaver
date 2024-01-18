package com.mygdx.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map) {
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

//        // Create map camera sections (where the camera will move to)
//        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
//            Rectangle rect = object.getRectangle();
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
//            body = world.createBody(bdef);
//            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
//            fdef.shape = shape;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = Constants.BIT_CAMERA;
//            body.createFixture(fdef).setUserData("camera_section");
//        }
    }

}
