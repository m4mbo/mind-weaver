package com.mygdx.Objects;

import com.mygdx.Sprites.B2Sprite;

// Every entity has a sprite attached to it
public abstract class Entity extends B2Sprite {
    protected int ID;

    protected Entity(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }
}
