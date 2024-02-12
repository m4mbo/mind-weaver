package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Tools.MyResourceManager;

public class SettingsScreen implements Screen {
    private final MyResourceManager resourceManager;
    private final ScreenManager screenManager;

    public SettingsScreen(MyResourceManager resourceManager, ScreenManager screenManager) {
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;
    }
    @Override
    public void dispose() {

    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


}
