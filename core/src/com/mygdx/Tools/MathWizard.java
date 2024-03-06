package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector2;

public class MathWizard {

    //calculate slope of line passing between two points
    public static float slope(Vector2 pos1, Vector2 pos2) {
        return (pos2.y - pos1.y) / (pos2.x - pos1.x);
    }

    //calculate angle in degrees of line passing between two points
    public static float angle(Vector2 pos1, Vector2 pos2) {
        return (float) (Math.atan(slope(pos1, pos2)) * (180 / Math.PI));
    }

    //preserve direction of vector by normalising to 1
    public static Vector2 normalize(Vector2 direction) {
        float length = pythagoras(direction.x, direction.y);
        if (length != 0) {
            direction.x /= length;
            direction.y /= length;
        }
        return direction;
    }

    //get length of hypotenuse using pythagoras's theorem
    public static float pythagoras(float a, float b) {
        return (float) Math.sqrt(a * a + b * b);
    }

    //calculate normalised direction vector from pos1 to pos2
    public static Vector2 normalizedDirection(Vector2 pos1, Vector2 pos2) {
        float pos1X = pos1.x;
        float pos1Y = pos1.y;

        float pos2X = pos2.x;
        float pos2Y = pos2.y;

        float directionX = pos1X - pos2X;
        float directionY = pos1Y - pos2Y;

        return normalize(new Vector2(directionX, directionY));
    }

    //calculate the distance between two points
    public static float distance(Vector2 pos1, Vector2 pos2) {
        return (float) Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
    }

    //checks if two float values are within a specified range of each other.
    public static boolean inRange(float p1, float p2, float threshold) {
        return (p1 >= p2 - threshold && p1 <= p2 + threshold);
    }

    //checks if the x and y coordinates of two vectors are within specified ranges of each other.
    public static boolean inRange(Vector2 pos1, Vector2 pos2, float threshold) {
        return inRange(pos1.x, pos2.x, threshold) && inRange(pos1.y, pos2.y, threshold);
    }

}
