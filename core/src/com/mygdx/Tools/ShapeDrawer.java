package com.mygdx.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Graphics.ShaderHandler;
import java.util.LinkedList;

public class ShapeDrawer {
    private LinkedList<Shape> shapes;
    private ShaderHandler shaderHandler;
    private MyResourceManager resourceManager;

    public ShapeDrawer(ShaderHandler shaderHandler, MyResourceManager resourceManager) {
        shapes = new LinkedList<>();
        this.shaderHandler = shaderHandler;
        this.resourceManager = resourceManager;
    }

    public void render(SpriteBatch batch) {
        for (Shape shape : shapes) {
            shape.render(batch);
        }
        shapes.clear();
    }

    public void drawWave(Vector2 start, Vector2 end, float height) {
        shapes.add(new Wave(start, end, height));
    }

    public void drawRectangle(float height, float width, float x, float y, String type) { shapes.add(new Rectangle(height, width, x, y, type)); }


    private interface Shape {
        void render(SpriteBatch batch);
    }

    private class Rectangle implements Shape {

        final float height;
        final float width;
        final float x;
        final float y;
        final String type;

        public Rectangle(float height, float width, float x, float y, String type) {
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
            this.type = type;
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.begin();
            if (type.equals("translucent")) {
                batch.draw(new TextureRegion(resourceManager.getTexture("translucent_pixel")), x, y, width, height);
            } else if (type.equals("gray")) {
                batch.draw(new TextureRegion(resourceManager.getTexture("gray_pixel")), x, y, width, height);
            }
            batch.end();
        }
    }

    private class Wave implements Shape {
        Vector2 start;
        Vector2 end;
        float height;
        float angle;
        float width;
        public Wave(Vector2 start, Vector2 end, float height) {
            this.start = start;
            this.end = end;
            this.height = height;
            angle = MathWizard.angle(start, end);
            width = MathWizard.distance(start, end);

        }

        @Override
        public void render(SpriteBatch batch) {
            batch.setShader(shaderHandler.getShaderProgram("wave"));
            batch.begin();
            batch.draw(new TextureRegion(resourceManager.getTexture("purple_pixel")), start.x, start.y, 0, 0, start.x > end.x ? -width : width, height, 1f, 1f, angle);
            batch.end();
            batch.setShader(null);
        }
    }
}
