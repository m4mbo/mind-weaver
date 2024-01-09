package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class MyResourceManager {

    // Hash map for efficient texture and sound lookup
    private HashMap<String, Texture> textures;
    private HashMap<String, Sound> sounds;

    public MyResourceManager() {
        textures = new HashMap<>();
        sounds = new HashMap<>();
    }
    public void loadTexture(String path, String key) {
        Texture texture = new Texture(Gdx.files.internal(path));
        textures.put(key, texture);
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public void disposeTexture(String key) {
        Texture texture = textures.get(key);
        if (texture != null) texture.dispose();
    }

    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key, sound);
    }

    public Sound getSound(String key) {
        return sounds.get(key);
    }

    public void disposeSound(String key) {
        Sound sound = sounds.get(key);
        if (sound != null) sound.dispose();
    }

}
