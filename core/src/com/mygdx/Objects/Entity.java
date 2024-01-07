package com.mygdx.Objects;

public abstract class Entity {
    private final int ID;

    protected Entity(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }
}
