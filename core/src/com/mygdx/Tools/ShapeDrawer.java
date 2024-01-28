package com.mygdx.Tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public void drawLine(Vector2 start, Vector2 end, float width, float height) {
        shapes.add(new Line(start, end, width, height));
    }

    private interface Shape {
        void render(SpriteBatch batch);
    }

    private class Line implements Shape {
        Vector2 start;
        Vector2 end;
        float width;
        float height;
        public Line(Vector2 start, Vector2 end, float width, float height) {
            this.start = start;
            this.end = end;
            this.width = width;
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.begin();
            batch.end();
        }
    }
}
