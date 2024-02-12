package com.mygdx.Screens;
import com.badlogic.gdx.Gdx;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Tools.MyResourceManager;
import com.badlogic.gdx.Screen;

public final class ScreenManager {

    private final MindWeaver game;
    private final MyResourceManager resourceManager;
    private GameInputProcessor gameInputProcessor;
    private Screen prevScreen;
    private Screen currScreen;

    public enum ScreenType {
        START, RESUME, LEVELS, LEVEL_1, SETTINGS, MENU, LEVEL_COMPLETE, EXIT
    }

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.currScreen = null;
    }

    public void setGameInputProcessor(GameInputProcessor gameInputProcessor) {
        this.gameInputProcessor = gameInputProcessor;
    }

    public void setCurrentScreen(ScreenType screenType) {
        // Dispose resources of the current screen if it exists
        if(prevScreen != null) {
            prevScreen.dispose();
        }

        // Dispose resources of the current screen if it exists
        if (currScreen != null) {
            prevScreen = currScreen;
            game.getScreen().dispose();
        }

        // Set the new screen
        switch (screenType) {
            case START:
                currScreen = new StartScreen(game, resourceManager, this);
                break;
            case RESUME:
                //game.setScreen(new GameScreen(game, 1, resourceManager, gameInputProcessor));
                break;
            case LEVELS:
                currScreen = new LevelsScreen(resourceManager, this);
                break;
            case LEVEL_1:
                currScreen = new GameScreen(game, 1, resourceManager, gameInputProcessor);
                break;
            case SETTINGS:
                currScreen = new SettingsScreen(resourceManager, this);
                break;
            case MENU:
                currScreen = new MenuScreen(resourceManager, this);
                break;
            case LEVEL_COMPLETE:
                currScreen = new LevelCompleteScreen(resourceManager, this);
                break;
            case EXIT:
                Gdx.app.exit();;
                break;
            default:
                break;
        }
    }

    private void disposeCurrentScreen() {
        game.getScreen().dispose();
    }

}


