package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class ColorGenerator {

    private Vector3 currColor;
    private final Random random;

    public ColorGenerator() {
        random = new Random();
        currColor = getRandomColor();
    }

    public void getNextColor() {
        Vector3 nextColor;
        do {
            nextColor = getRandomColor();
        } while (nextColor.equals(currColor));
        currColor = nextColor;
    }

    private Vector3 getRandomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Vector3(r / 255f, g / 255f, b / 255f);
    }

    public Vector3 getCurrentColor() {
        return currColor;
    }

}
