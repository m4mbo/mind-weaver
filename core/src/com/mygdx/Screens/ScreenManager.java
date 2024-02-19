package com.mygdx.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
    private FrameBuffer fb;

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.currScreen = null;
        transitionQueue = new LinkedList<>();
        fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    public void pushScreen(Constants.SCREEN_OP screenType, String flag) {

        handleTransition(flag);

        if (screenType == Constants.SCREEN_OP.RESUME) {
            ManagedScreen temp = currScreen;
            currScreen = prevScreen;
            prevScreen = temp;
            game.setScreen(currScreen);
            currScreen.resume();
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
                currScreen = new GameScreen(game, level, resourceManager, this);
                break;
            case LEVELS:
                currScreen = new LevelsScreen(game, resourceManager, this);
                break;
            case LEVEL_1:
                currScreen = new GameScreen(game, 1, resourceManager, this);
                break;
            case LEVEL_2:
                currScreen = new GameScreen(game, 2, resourceManager, this);
                break;
            case LEVEL_3:
                currScreen = new GameScreen(game, 3, resourceManager, this);
                break;
            case LEVEL_4:
                currScreen = new GameScreen(game, 4, resourceManager, this);
                break;
            case LEVEL_5:
                //currScreen = new GameScreen(game, 5, resourceManager, this);
                break;
            case SETTINGS:
                currScreen = new SettingsScreen(resourceManager, this);
                break;
            case MENU:
                currScreen = new MenuScreen(resourceManager, this, game);
                break;
            case LEVEL_COMPLETE:
                currScreen = new LevelCompleteScreen(resourceManager, this);
                break;
            case EXIT:
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

    public void render(SpriteBatch batch, float delta) {

        LinkedList<Transition> toRemove = new LinkedList<>();

        for (Transition transition : transitionQueue) {
            if (transition.isDone()) toRemove.add(transition);
            transition.render(batch, delta);
        }

        transitionQueue.remove(toRemove);
    }

}


