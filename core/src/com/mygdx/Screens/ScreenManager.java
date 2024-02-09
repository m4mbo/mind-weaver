package com.mygdx.Screens;

import com.badlogic.gdx.Screen;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Tools.MyResourceManager;

import java.util.HashMap;

public final class ScreenManager {
    private final MindWeaver game;
    private MyResourceManager resourceManager;

    private GameInputProcessor inputProcessor
    private Screen prevScreen;
    private Screen currScreen;

    public ScreenManager (MindWeaver game, MyResourceManager resourceManager, )
    @Override
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
    }
}
