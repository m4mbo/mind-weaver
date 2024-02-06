package com.mygdx.Graphics;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;

public class LightManager {

    private RayHandler rayHandler;
    public LightManager(World world) {
        rayHandler = new RayHandler(world);
    }

    public void setDim(float scale) { rayHandler.setAmbientLight(scale); }

    public void update(OrthographicCamera camera) {
        rayHandler.setCombinedMatrix(camera);
        rayHandler.update();
    }

    public void render() {
        rayHandler.render();
    }

    public void addLight(Body body, float distance, short maskBits, Color color) {
        PointLight pointLight = new PointLight(rayHandler, 30, color, distance / Constants.PPM, 0, 0);
        pointLight.attachToBody(body);
        pointLight.setSoftnessLength(0);
        pointLight.setContactFilter(Constants.BIT_LIGHT, (short) 0, maskBits);
    }

    public void addLight(float x, float y, float distance, Color color) {
        PointLight pointLight = new PointLight(rayHandler, 100, color, distance / Constants.PPM, x, y);
    }

    public void addLight(float x, float y, float distance, short maskBits, Color color) {
        PointLight pointLight = new PointLight(rayHandler, 100, color, distance / Constants.PPM, x, y);
    }


}
