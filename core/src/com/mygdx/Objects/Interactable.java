package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.Sprites.B2Sprite;
import com.mygdx.Tools.MyResourceManager;

import java.util.LinkedList;

public abstract class Interactable extends B2Sprite {

    protected LinkedList<Reactable> reactables;
    protected World world;
    protected MyResourceManager resourceManager;
    protected Constants.ASTATE currAState;     // Current animation state
    protected Constants.ASTATE prevAState;     // Previous animation state

    public Interactable(World world, MyResourceManager resourceManager) {
        reactables = new LinkedList<>();
        this.world = world;
        this.resourceManager = resourceManager;
    }

    public void interact() {
        for (Reactable reactable : reactables) {
            reactable.react();
        }
    }

    public void addReactable(Reactable reactable) {
        reactables.add(reactable);
    }

}
