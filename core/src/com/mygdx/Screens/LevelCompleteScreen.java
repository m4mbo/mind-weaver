package com.mygdx.Screens;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.Tools.MyResourceManager;

public class LevelCompleteScreen extends ManagedScreen {
    private ScreenManager screenManager;
    private Stage stage;

    public LevelCompleteScreen(MyResourceManager resourceManager, ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
    @Override
    public void render(float v) {

    }
    @Override
    public void dispose() {

    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

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
