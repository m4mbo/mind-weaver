package com.mygdx.Screens;
import com.badlogic.gdx.Gdx;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.badlogic.gdx.Screen;

public final class ScreenManager {
    private final MindWeaver game;
    private final MyResourceManager resourceManager;
    private Screen prevScreen;
    private Screen currScreen;

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.currScreen = null;

    }

    public void pushScreen(Constants.SCREEN_TYPE screenType) {

        if (screenType == Constants.SCREEN_TYPE.RESUME) {
            currScreen.dispose();
            currScreen = prevScreen;
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

        // Set the new screen
        switch (screenType) {
            case START:
                currScreen = new StartScreen(game, resourceManager, this);
                break;
            case RESTART:
                int level = ((GameScreen) prevScreen).getLevel();
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

        game.setScreen(currScreen);
    }
}


