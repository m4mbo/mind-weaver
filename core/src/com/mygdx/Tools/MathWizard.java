package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector2;

public class MathWizard {

    public static float slope(Vector2 pos1, Vector2 pos2) {
        return (pos2.y - pos1.y) / (pos2.x - pos1.x);
    }

    public static float angle(Vector2 pos1, Vector2 pos2) {
        return (float) (Math.atan(slope(pos1, pos2)) * (180 / Math.PI));
    }

    public static Vector2 normalize(Vector2 direction) {
        float length = pythagoras(direction.x, direction.y);
        if (length != 0) {
            direction.x /= length;
            direction.y /= length;
        }
        return direction;
    }

    public static float pythagoras(float a, float b) {
        return (float) Math.sqrt(a * a + b * b);
    }

    public static Vector2 normalizedDirection(Vector2 pos1, Vector2 pos2) {
        float pos1X = pos1.x;
        float pos1Y = pos1.y;

        float pos2X = pos2.x;
        float pos2Y = pos2.y;

        float directionX = pos1X - pos2X;
        float directionY = pos1Y - pos2Y;

        return normalize(new Vector2(directionX, directionY));
    }

    public static float distance(Vector2 pos1, Vector2 pos2) {
        return (float) Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
    }


}
