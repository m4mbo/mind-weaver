package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.Sprites.B2Sprite;
import com.mygdx.Tools.MyResourceManager;

public abstract class Reactable extends B2Sprite {

    protected World world;
    protected MyResourceManager resourceManager;
    protected Constants.ASTATE currAState;     // Current animation state
    protected Constants.ASTATE prevAState;     // Previous animation state

    public Reactable(World world, MyResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.world = world;
    }

    public void react() {

    }

}
