package com.mygdx.Objects;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Sprites.B2Sprite;

// Every entity has a sprite attached to it
public abstract class Entity extends B2Sprite {
    protected int ID;
    protected final MyResourceManager resourceManager;

    protected Entity(int ID, MyResourceManager resourceManager) {
        this.ID = ID;
        this.resourceManager = resourceManager;
    }

    public int getID() {
        return ID;
    }

    public void die() {}
}
