package com.mygdx.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Graphics.ShaderHandler;

import java.util.LinkedList;

public class ShapeDrawer {
    private LinkedList<Shape> shapes;
    private ShaderHandler shaderHandler;
    public ShapeDrawer(ShaderHandler shaderHandler) {
        shapes = new LinkedList<>();
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
            angle = MathWizard.angle(start, end);
            width = MathWizard.distance(start, end);

        }

        @Override
        public void render(SpriteBatch batch) {
            Texture texture = new Texture("Shapes/purple_pixel.png");
            batch.setShader(shaderHandler.getShaderProgram("wave"));
            batch.begin();
            batch.draw(new TextureRegion(texture), start.x, start.y, 0, 0, start.x > end.x ? -width : width, height, 1f, 1f, angle);
            batch.end();
            batch.setShader(null);
            texture.dispose();
        }
    }
}
