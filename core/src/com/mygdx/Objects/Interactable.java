package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Sprites.B2Sprite;
import java.util.LinkedList;

public abstract class Interactable extends B2Sprite {

    private LinkedList<Reactable> reactables;
    protected World world;
    protected Body b2body;

    public Interactable(World world) {
        reactables = new LinkedList<>();
        this.world = world;
    }

    public void interact() {
        for (Reactable reactable : reactables) {
            reactable.react();
        }
    }

}
