package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Helpers.Constants;
import com.mygdx.Objects.Item;
import com.mygdx.RoleCast.Mage;
import java.util.LinkedList;

public class HUD {

    LinkedList<Item> inventory;
    Mage player;

    public HUD() {
        inventory = new LinkedList<>();
    }

    public void setPlayer(Mage player) {
        this.player = player;
    }

    public void render(SpriteBatch batch) {

    }

}
