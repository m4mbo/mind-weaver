package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Graphics.ShaderHandler;

public class Item {

    private final String name;
    private final ShaderHandler shaderHandler;

    public Item(String name, String name1, ShaderHandler shaderHandler) {
        this.name = name1;
        this.shaderHandler = shaderHandler;
    }

    public void render(SpriteBatch batch) {

    }

}
