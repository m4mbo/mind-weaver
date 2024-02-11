package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Tools.MyResourceManager;

public final class ScreenManager {

    private MindWeaver game;
    private MyResourceManager resourceManager;
    private GameInputProcessor gameInputProcessor;
    private ScreenType currentScreen;

    public enum ScreenType {
        START, RESUME, LEVELS, SETTINGS, MENU, LEVELCOMPLETE, EXIT
    }

    public ScreenManager(MindWeaver game, MyResourceManager resourceManager, GameInputProcessor gameInputProcessor) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.gameInputProcessor = gameInputProcessor;
        this.currentScreen = null;
    }

    public void setCurrentScreen(ScreenType screenType) {
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
                //game.setScreen(new GameScreen(game, "everlush", resourceManager, game.getGameInputProcessor()));
            case LEVELS:
                game.setScreen(new LevelsScreen(resourceManager, this));
                break;
            case SETTINGS:
                game.setScreen(new SettingsScreen(resourceManager, this));
                break;
            case MENU:
                game.setScreen(new MenuScreen(resourceManager, this));
            case LEVELCOMPLETE:
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

    public ScreenType getCurrentScreen() {
        return currentScreen;
    }
}

        /*@Override
        public void create() {
        screens = new HashMap<>();
        ScreenType.START.getScreen(this).build();
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
