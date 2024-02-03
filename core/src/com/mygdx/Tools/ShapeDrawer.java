package com.mygdx.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Handlers.ShaderHandler;

import java.util.LinkedList;

public class ShapeDrawer {
    private LinkedList<Shape> shapes;
    private ShaderHandler shaderHandler;
    private SpriteBatch batch;
    public ShapeDrawer(ShaderHandler shaderHandler, SpriteBatch batch) {
        shapes = new LinkedList<>();
        this.batch = batch;
        this.shaderHandler = shaderHandler;
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

    private interface Shape {
        void render(SpriteBatch batch);
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
            float slope = (end.y - start.y) / (end.x - start.x);
            angle = (float) (Math.atan(slope) * (180 / Math.PI));
            width = (float) Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));

        }

        @Override
        public void render(SpriteBatch batch) {
            batch.setShader(shaderHandler.getShaderProgram("wave"));
            batch.begin();
            batch.draw(new TextureRegion(new Texture("Shapes/purple_pixel.png")), start.x, start.y, 0, 0, start.x > end.x ? -width : width, height, 1f, 1f, angle);
            batch.end();
            batch.setShader(null);
        }
    }
}
