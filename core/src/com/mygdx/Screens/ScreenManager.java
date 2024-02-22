package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.mygdx.Audio.MusicManager;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Graphics.Transitions.SlidingTransition;
import com.mygdx.Graphics.Transitions.Transition;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import java.util.LinkedList;
import java.util.Queue;

public final class ScreenManager {
    private final MindWeaver game;
    private final MyResourceManager resourceManager;
    private final Queue<Transition> transitionQueue;
    private ManagedScreen prevScreen;
    private ManagedScreen currScreen;
    private int level;
    private int levelProgression;
    private final FrameBuffer fb;
    private final MusicManager musicManager;

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager, MusicManager musicManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.currScreen = null;
        transitionQueue = new LinkedList<>();
        fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        levelProgression = 1;
        this.musicManager = musicManager;

        musicManager.play("Music/main_theme.mp3");
    }

    public void pushScreen(Constants.SCREEN_OP screenType, String flag) {

        handleTransition(flag);

        if (screenType == Constants.SCREEN_OP.RESUME) {
            musicManager.setVolume(1);
            ManagedScreen temp = currScreen;
            currScreen = prevScreen;
            prevScreen = temp;
            game.setScreen(currScreen);
            currScreen.resume();
            return;
        }  else if (screenType == Constants.SCREEN_OP.CONTROLS) {
            musicManager.setVolume(1);
            ManagedScreen temp = currScreen;
            currScreen = prevScreen;
            prevScreen = temp;
            game.setScreen(currScreen);
            currScreen.resume();
            game.hud.pushCutscene("lesson");
            return;
        }

        // Dispose resources of the current screen if it exists
        if(prevScreen != null) {
            prevScreen.dispose();
        }

        // Dispose resources of the current screen if it exists
        if (currScreen != null) {
            prevScreen = currScreen;
        }

        if (currScreen instanceof GameScreen) level = ((GameScreen) currScreen).getLevel();

        // Set the new screen
        switch (screenType) {
            case START:
                currScreen = new StartScreen(game, resourceManager, this);
                break;
            case RESTART:
                game.hud.removeCutscene();
                game.hud.removePapaya(level);
                currScreen = new GameScreen(game, level, resourceManager, this, musicManager);
                break;
            case LEVELS:
                musicManager.setVolume(1);
                currScreen = new LevelsScreen(resourceManager, this, levelProgression);
                break;
            case LEVEL_1:
                currScreen = new GameScreen(game, 1, resourceManager, this, musicManager);
                break;
            case LEVEL_2:
                currScreen = new GameScreen(game, 2, resourceManager, this, musicManager);
                break;
            case LEVEL_3:
                currScreen = new GameScreen(game, 3, resourceManager, this, musicManager);
                break;
            case LEVEL_4:
                currScreen = new GameScreen(game, 4, resourceManager, this, musicManager);
                break;
            case LEVEL_5:
                currScreen = new GameScreen(game, 5, resourceManager, this, musicManager);
                break;
            case MENU:
                musicManager.setVolume(0.2f);
                assert currScreen != null;
                currScreen = new MenuScreen(resourceManager, this, currScreen.screenToTexture(fb));
                break;
            case EXIT:
                resourceManager.disposeAll();
                Gdx.app.exit();
                break;
            default:
                break;
        }

        game.setScreen(currScreen);
    }

    public void handleTransition(String flag) {
        switch (flag){
            case "slide_up":
                transitionQueue.add(new SlidingTransition(new TextureRegion(currScreen.screenToTexture(fb)), 3, 80, Constants.SLIDE_DIR.SLIDE_UP, currScreen.getProjectionMatrix()));
                break;
            case "slide_down":
                transitionQueue.add(new SlidingTransition(new TextureRegion(currScreen.screenToTexture(fb)), 3, 80, Constants.SLIDE_DIR.SLIDE_DOWN, currScreen.getProjectionMatrix()));
                break;
            case "slide_left":
                transitionQueue.add(new SlidingTransition(new TextureRegion(currScreen.screenToTexture(fb)), 3, 80, Constants.SLIDE_DIR.SLIDE_LEFT, currScreen.getProjectionMatrix()));
                break;
            case "slide_right":
                transitionQueue.add(new SlidingTransition(new TextureRegion(currScreen.screenToTexture(fb)), 3, 80, Constants.SLIDE_DIR.SLIDE_RIGHT, currScreen.getProjectionMatrix()));
                break;
            default:
                break;
        }
    }

    public void render(float delta) {

        LinkedList<Transition> toRemove = new LinkedList<>();

        for (Transition transition : transitionQueue) {
            if (transition.isDone()) toRemove.add(transition);
            transition.render(game.batch, delta);
        }

        transitionQueue.remove(toRemove);
    }

    public void setLevelProgression(int progression) {
        if (progression > levelProgression) {
            levelProgression = progression;
        }
    }

}


