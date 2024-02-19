package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class LevelsScreen implements Screen {
    private final MindWeaver game;
    private final ScreenManager screenManager;
    private Texture levelsTexture;

    public LevelsScreen(MindWeaver game, MyResourceManager resourceManager, ScreenManager screenManager) {

        this.game = game;
        this.screenManager = screenManager;

        initLevelsScreen(resourceManager);
    }

    public void initLevelsScreen(MyResourceManager resourceManager) {
        levelsTexture = resourceManager.getTexture("LevelsScreen");
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(levelsTexture, (Gdx.graphics.getWidth() - levelsTexture.getWidth()*5)/2, (Gdx.graphics.getHeight() - levelsTexture.getHeight()*5)/2, levelsTexture.getWidth()*5, levelsTexture.getHeight()*5);

        game.batch.end();
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_1);

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_2);

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_3);

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_4);

        //if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_5);

    }

    public void update(float delta) {
        handleInput();
    }

    @Override
    public void dispose() { }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

}
