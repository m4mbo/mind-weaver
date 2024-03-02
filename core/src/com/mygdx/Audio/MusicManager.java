package com.mygdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private Music currTrack;        // Track being played
    public MusicManager() {}

    public void play(String path) {
        // Dispose of the previous track
        if (currTrack != null) {
            currTrack.stop();
            currTrack.dispose();
        }
        currTrack = Gdx.audio.newMusic(Gdx.files.internal(path));   //Set new track to play
        currTrack.setLooping(true);                                 //Repeat playing track
        currTrack.play();                                           //Start playing track
    }

    public void stop() {
        currTrack.stop();
    }   //Stop playing track

    public void resume() {
        currTrack.play();
    }   //Start playing track

    public void setVolume(float volume) {
        currTrack.setVolume(volume);
    }   //Set track volume

}
