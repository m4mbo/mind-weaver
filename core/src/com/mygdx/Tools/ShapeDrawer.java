package com.mygdx.Tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.LinkedList;

public class ShapeDrawer {

    private ShapeRenderer sr;
    private LinkedList<Shape> shapes;
    private OrthographicCamera gameCam;

    public ShapeDrawer(OrthographicCamera gameCam) {
        shapes = new LinkedList<>();
        sr = new ShapeRenderer();
        this.gameCam = gameCam;
    }

    public void render(SpriteBatch batch) {
        for (Shape shape : shapes) {
            shape.render(batch);
        }
        shapes.clear();
    }

    public void drawLine(Vector2 start, Vector2 end, float width) {
        shapes.add(new Line(start, end, width));
    }

    private interface Shape {
        void render(SpriteBatch batch);
    }

    private class Line implements Shape {
        Vector2 start;
        Vector2 end;
        float width;
        public Line(Vector2 start, Vector2 end, float width) {
            this.start = start;
            this.end = end;
            this.width = width;
        }

        @Override
        public void render(SpriteBatch batch) {
            sr.setProjectionMatrix(gameCam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.line(start, end);
            sr.setColor(1, 0, 0, 1);
            sr.end();
        }
    }

    private class SineTerm {
        private final float amplitude;
        private final float waveLength;
        private final float phaseDifference;

        public SineTerm(float amplitude, float waveLength, float phaseDifference) {
            this.amplitude = amplitude;
            this.waveLength = waveLength;
            this.phaseDifference = phaseDifference;
        }

        public float evaluate(float x) {
            return amplitude * (float) Math.sin(2 * Math.PI * x / waveLength + phaseDifference);
        }
    }

}
