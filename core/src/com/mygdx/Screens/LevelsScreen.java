package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class LevelsScreen implements Screen {
    private final MindWeaver game;
    private final ScreenManager screenManager;
    private final float buttonWidth, buttonHeight;
    private ImageButton playButton;
    private Skin playSkin;
    private Stage stage;

    public LevelsScreen(MindWeaver game, MyResourceManager resourceManager, ScreenManager screenManager) {

        this.game = game;
        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        initLevelsScreen(resourceManager);
    }

    public void initLevelsScreen(MyResourceManager resourceManager) {

        playSkin = new Skin();
        playSkin.add("UnclickedPlayButton", resourceManager.getTexture("UnclickedPlayButton"));
        playSkin.add("ClickedPlayButton", resourceManager.getTexture("ClickedPlayButton"));

        ImageButton.ImageButtonStyle playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageUp = playSkin.getDrawable("UnclickedPlayButton");
        playButtonStyle.imageDown = playSkin.getDrawable("ClickedPlayButton");

        final ImageButton playButton = new ImageButton(playButtonStyle);
        playButton.setPosition((Gdx.graphics.getWidth() - playButton.getWidth())/2 , (Gdx.graphics.getHeight() - playButton.getHeight())/2 - 50);
        playButton.getImageCell().size(buttonWidth, buttonHeight);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButton.setChecked(false);
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_1);
            }
        });

        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void handleInput() {

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
