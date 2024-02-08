package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Tools.MyResourceManager;

public class SettingsScreen implements Screen {
    private final MindWeaver game;
    private final MyResourceManager resourceManager;

    public SettingsScreen(MindWeaver game, MyResourceManager resourceManager) {
        this.game = game;
        this.resourceManager = resourceManager;
    }

    // Other methods...

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;
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

    @Override
    public void dispose() {

    }

    // Other Screen methods...
}
