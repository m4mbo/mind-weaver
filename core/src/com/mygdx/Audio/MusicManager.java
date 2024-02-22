package com.mygdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private Music currTrack;
    public MusicManager() {}

    public void play(String path) {
        if (currTrack != null) {
            currTrack.stop();
            currTrack.dispose();
        }
        currTrack = Gdx.audio.newMusic(Gdx.files.internal(path));
        currTrack.setLooping(true);
        currTrack.play();
    }

    public void stop() {
        currTrack.stop();
    }

    public void resume() {
        currTrack.play();
    }

    public void setVolume(float volume) {
        currTrack.setVolume(volume);
    }

}
