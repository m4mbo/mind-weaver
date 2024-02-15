package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Tools.MyResourceManager;

public class SettingsScreen extends ManagedScreen {
    private final MyResourceManager resourceManager;
    private final ScreenManager screenManager;
    private Stage stage;

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

    @Override
    public Matrix4 getProjectionMatrix() {
        return stage.getBatch().getProjectionMatrix();
    }

}
