package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StartScreen implements Screen {
    private final MindWeaver game;
    private final ScreenManager screenManager;
    private Stage stage;
    private TextButton settingsButton, exitButton;
    private ImageButton playButton;
    private Skin playSkin, settingsSkin, exitSkin;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont titleFont;
    private final float buttonWidth, buttonHeight;
    private GlyphLayout layout;
    private CharSequence text;
    private final List<TextButton> startScreenButtons = new ArrayList<>();

    public StartScreen(MindWeaver game, MyResourceManager resourceManager, ScreenManager screenManager) {

        this.game = game;
        this.screenManager = screenManager;
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;
        this.stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);


        initStartScreen(resourceManager);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        game.batch.begin();

        titleFont.draw(game.batch, text, (Gdx.graphics.getWidth() - layout.width)/2, (Gdx.graphics.getHeight() - layout.height)/2 + 650);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        game.batch.end();
    }
    private void initStartScreen(MyResourceManager resourceManager) {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/KnightWarrior.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 300;
        titleFont = generator.generateFont(parameter);
        titleFont.setColor(112/255f, 41/255f, 99/255f, 1);

        text = "Mind Weaver";
        layout = new GlyphLayout(titleFont, text);

        playSkin = new Skin();
        settingsSkin = new Skin();
        exitSkin = new Skin();

        playSkin.add("UnclickedPlayButton", resourceManager.getTexture("UnclickedPlayButton"));
        playSkin.add("ClickedPlayButton", resourceManager.getTexture("ClickedPlayButton"));
        settingsSkin.add("UnclickedSettingsButton", resourceManager.getTexture("UnclickedSettingsButton"));
        settingsSkin.add("ClickedSettingsButton", resourceManager.getTexture("ClickedSettingsButton"));
        exitSkin.add("UnclickedExitButton", resourceManager.getTexture("UnclickedExitButton"));
        exitSkin.add("ClickedExitButton", resourceManager.getTexture("ClickedExitButton"));

        ImageButton.ImageButtonStyle PlayButtonStyle = new ImageButton.ImageButtonStyle();
        TextButton.TextButtonStyle SettingsButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle ExitButtonStyle = new TextButton.TextButtonStyle();

        PlayButtonStyle.up = playSkin.getDrawable("UnclickedPlayButton");
        PlayButtonStyle.down = playSkin.getDrawable("ClickedPlayButton");
        SettingsButtonStyle.up = settingsSkin.getDrawable("UnclickedSettingsButton");
        SettingsButtonStyle.down = settingsSkin.getDrawable("ClickedSettingsButton");
        ExitButtonStyle.up = exitSkin.getDrawable("UnclickedExitButton");
        ExitButtonStyle.down = exitSkin.getDrawable("ClickedExitButton");

        BitmapFont font = new BitmapFont();
        SettingsButtonStyle.font = font;
        ExitButtonStyle.font = font;

        playButton = new ImageButton(playSkin);
        PlayButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(resourceManager.getTexture("UnclickedPlayButton")));
        playButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 - 50);
        playButton.setSize(buttonWidth, buttonHeight);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButton.getStyle().down = playSkin.getDrawable("ClickedPlayButton");
                playButton.setStyle(playButton.getStyle());
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVELS);
            }
        });

        settingsButton = new TextButton(" ", SettingsButtonStyle);
        settingsButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight) / 2 - 250);
        settingsButton.setSize(buttonWidth, buttonHeight);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsButton.getStyle().down = settingsSkin.getDrawable("ClickedSettingsButton");
                settingsButton.setStyle(settingsButton.getStyle());
                screenManager.pushScreen(Constants.SCREEN_TYPE.SETTINGS);
            }
        });

        exitButton = new TextButton(" ", ExitButtonStyle);
        exitButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight) / 2 - 450);
        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitButton.getStyle().down = exitSkin.getDrawable("ClickedExitButton");
                exitButton.setStyle(exitButton.getStyle());
                screenManager.pushScreen(Constants.SCREEN_TYPE.EXIT);
            }
        });

        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);

    }

    /*public void initButton(TextButton.TextButtonStyle textButtonStyle, ) {

    }*/

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
