package com.mygdx.Handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Objects.PlayableCharacter;

import java.util.LinkedList;

public class PlayerController {

    private final LinkedList<PlayableCharacter> characters;
    private PlayableCharacter currCharacter;

    public PlayerController(PlayableCharacter currCharacter) {
        characters = new LinkedList<>();
        this.currCharacter = currCharacter;
        characters.add(currCharacter);
    }

    public PlayableCharacter getCharacter() {
        return currCharacter;
    }

    public void addCharacter(PlayableCharacter character) { characters.add(character); }

    public void setCharacter(PlayableCharacter character) {
        this.currCharacter = character;
    }

    public void update(float delta) {
        for (PlayableCharacter character : characters) {
            character.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (PlayableCharacter character : characters) {
            character.render(batch);
        }
    }

}
