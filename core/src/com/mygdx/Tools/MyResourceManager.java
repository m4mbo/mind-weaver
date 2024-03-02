    package com.mygdx.Tools;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.audio.Sound;
    import com.badlogic.gdx.graphics.Texture;

    import java.util.HashMap;

    public class MyResourceManager {

        // Hash map for efficient texture and sound lookup
        private final HashMap<String, Texture> textures;
        private final HashMap<String, Sound> sounds;

        public MyResourceManager() {
            textures = new HashMap<>();
            sounds = new HashMap<>();
        }
        //Loads texture from assets and places it in a hash map
        public void loadTexture(String path, String key) {
            Texture texture = new Texture(Gdx.files.internal(path));
            textures.put(key, texture);
        }

        //Gets texture from hash map based on unique texture key
        public Texture getTexture(String key) {
            return textures.get(key);
        }

        //Loads sound from assets and places it in a hash map
        public void loadSound(String path, String key) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(key, sound);
        }

        //Gets sound from hash map based on unique sound key
        public Sound getSound(String key) {
            return sounds.get(key);
        }

        //Method to dispose all textures and sounds from both hash maps
        public void disposeAll() {
            for (Sound sound : sounds.values()) {
                sound.dispose();
            }
            for (Texture texture : textures.values()) {
                texture.dispose();
            }
        }

    }
