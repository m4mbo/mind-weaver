package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class ColourGenerator {   //class to generate random colours

    private Vector3 currColor;
    private final Random random;

    public ColourGenerator() {
        random = new Random();
        currColor = getRandomColour();
    }

    //keep generating a new random colour until the next colour is not the same as current colour
    public void getNextColor() {
        Vector3 nextColor;
        do {
            nextColor = getRandomColour();
        } while (nextColor.equals(currColor));
        currColor = nextColor;
    }

    //return randomly generated RGB values
    private Vector3 getRandomColour() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Vector3(r / 255f, g / 255f, b / 255f);
    }

    //get current colour of colour generator
    public Vector3 getCurrentColour() {
        return currColor;
    }

}
