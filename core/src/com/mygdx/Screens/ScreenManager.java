package com.mygdx.Screens;
import com.badlogic.gdx.Gdx;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Tools.MyResourceManager;

public final class ScreenManager {

    private final MindWeaver game;
    private final MyResourceManager resourceManager;
    private GameInputProcessor gameInputProcessor;
    private SCREEN_TYPE currentScreen;

    public enum SCREEN_TYPE {
        START, RESUME, LEVELS, LEVEL1, SETTINGS, MENU, LEVEL_COMPLETE, EXIT
    }

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.currentScreen = null;
    }

    public void setGameInputProcessor(GameInputProcessor gameInputProcessor) {
        this.gameInputProcessor = gameInputProcessor;
    }

    public void setCurrentScreen(SCREEN_TYPE screenType) {
        // Dispose resources of the current screen if it exists
        if (currentScreen != null) {
            disposeCurrentScreen();
        }

        // Set the new screen
        switch (screenType) {
            case START:
                game.setScreen(new StartScreen(game, resourceManager, this));
                break;
            case RESUME:
                //game.setScreen(new GameScreen(game, 1, resourceManager, gameInputProcessor));
                break;
            case LEVELS:
                game.setScreen(new LevelsScreen(resourceManager, this));
                break;
            case LEVEL1:
                game.setScreen(new GameScreen(game, 1, resourceManager, gameInputProcessor));
                break;
            case SETTINGS:
                game.setScreen(new SettingsScreen(resourceManager, this));
                break;
            case MENU:
                game.setScreen(new MenuScreen(resourceManager, this));
                break;
            case LEVEL_COMPLETE:
                game.setScreen(new LevelCompleteScreen(resourceManager, this));
            case EXIT:
                Gdx.app.exit();;
            default:
                break;
        }

        currentScreen = screenType;
    }

    private void disposeCurrentScreen() {
        game.getScreen().dispose();
    }

    public SCREEN_TYPE getCurrentScreen() {
        return currentScreen;
    }
}

        /*@Override
        public void create() {
        screens = new HashMap<>();
        SCREEN_TYPE.START.getScreen(this).build();
        setScreen(MENU);
        }

     public void pushScreen() {
        if (prevScreen)
        setScreen(m);
        }

        @Override
    public void dispose() {
        super.dispose();
        for (Screen screen : screens.values()) {
            screen.dispose();
        }
    }*/
