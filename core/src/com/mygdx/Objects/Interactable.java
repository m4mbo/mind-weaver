package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.Sprites.B2Sprite;
import com.mygdx.Tools.MyResourceManager;
import java.util.LinkedList;

// Class from where levers and pressure plate will inherit from
public abstract class Interactable extends B2Sprite {

    protected LinkedList<Reactable> reactables;     // List of reactables linked to interactable
    protected World world;
    protected MyResourceManager resourceManager;
    protected Constants.ASTATE currAState;     // Current animation state
    protected Constants.ASTATE prevAState;     // Previous animation state

    public Interactable(World world, MyResourceManager resourceManager) {
        reactables = new LinkedList<>();
        this.world = world;
        this.resourceManager = resourceManager;
    }

    // Notifying all reactables
    public void interact() {
        for (Reactable reactable : reactables) {
            reactable.react();
        }
    }

    //Keep track of what reactable will react according to interaction with interactable
    public void addReactable(Reactable reactable) {
        reactables.add(reactable);
    }

}
