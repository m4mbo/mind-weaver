package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class LevelsScreen implements Screen {
    private final ScreenManager screenManager;
    private final float buttonWidth, buttonHeight;
    private TextButton playButton;
    private Skin playSkin;
    private final Stage stage;

    public LevelsScreen(MindWeaver game, MyResourceManager resourceManager, final ScreenManager screenManager) {

        this.screenManager = screenManager;
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        playSkin = new Skin();

        playSkin.add("UnclickedPlayButton", resourceManager.getTexture("UnclickedPlayButton"));
        playSkin.add("ClickedPlayButton", resourceManager.getTexture("ClickedPlayButton"));

        TextButton.TextButtonStyle PlayButtonStyle = new TextButton.TextButtonStyle();

        PlayButtonStyle.up = playSkin.getDrawable("UnclickedPlayButton");
        PlayButtonStyle.down = playSkin.getDrawable("ClickedPlayButton");

        BitmapFont font = new BitmapFont();
        PlayButtonStyle.font = font;

        playButton = new TextButton(" ", PlayButtonStyle);
        playButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 - 50);
        playButton.setSize(buttonWidth, buttonHeight);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButton.getStyle().down = playSkin.getDrawable("ClickedPlayButton");
                playButton.setStyle(playButton.getStyle());

                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_1);
            }
        });

        stage.addActor(playButton);
    }

    @Override
    public void show() {

    }

    public void update(float delta) {
        handleInput();
    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }
    @Override
    public void dispose() {
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
