package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Sprites.B2Sprite;

public abstract class Reactable extends B2Sprite {

    protected World world;
    protected Body b2body;
    protected Fixture fixture;

    public Reactable(World world) {
        this.world = world;
    }

    public void react() {

    }

}
