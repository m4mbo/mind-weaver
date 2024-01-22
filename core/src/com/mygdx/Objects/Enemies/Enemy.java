package com.mygdx.Objects.Enemies;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Objects.Entity;

public abstract class Enemy extends Entity {

    protected World world;


    public Enemy (int x, int y, World world, int id, MyResourceManager resourceManager) {
        super(id,resourceManager);


    }
}
