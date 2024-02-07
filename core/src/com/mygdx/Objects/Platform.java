package com.mygdx.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Tools.MyResourceManager;

public class Platform extends Reactable {
    public Platform(World world, MyResourceManager resourceManager) {
        super(world, resourceManager);
    }
}
