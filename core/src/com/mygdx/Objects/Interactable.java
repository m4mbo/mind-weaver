package com.mygdx.Objects;

import com.mygdx.Sprites.B2Sprite;
import java.util.LinkedList;

public abstract class Interactable extends B2Sprite {

    private LinkedList<Reactable> reactables;   

    public Interactable() {
        reactables = new LinkedList<>();
    }

    public void interact() {
        for (Reactable reactable : reactables) {
            reactable.react();
        }
    }

}
