package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StartScreen extends ManagedScreen {
    private final MindWeaver game;
    private final ScreenManager screenManager;
    private Stage stage;
    private ImageButton playButton, settingsButton, exitButton;
    private Skin playSkin, settingsSkin, exitSkin;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont titleFont;
    private final float buttonWidth, buttonHeight;
    private GlyphLayout layout;
    private CharSequence text;
    private Array<ImageButton> buttons;
    public StartScreen(MindWeaver game, MyResourceManager resourceManager, ScreenManager screenManager) {

        this.game = game;
        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        initStartScreen(resourceManager);
    }

    public ImageButton initButton(final Skin skin, final String unclickedImagePath, final String clickedImagePath, int offset, final float width, final float height, final Constants.SCREEN_OP screenType) {
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = skin.getDrawable(unclickedImagePath);
        buttonStyle.imageDown = skin.getDrawable(clickedImagePath);

        final ImageButton button = new ImageButton(buttonStyle);
        button.setPosition((Gdx.graphics.getWidth() - button.getWidth())/2 , (Gdx.graphics.getHeight() - button.getHeight())/2 - offset);
        button.getImageCell().size(width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.setChecked(false);
                screenManager.pushScreen(screenType,"slide_left");
            }
        });
        buttons.add(button);
        stage.addActor(button);

        return button;
    }

    private void initStartScreen(MyResourceManager resourceManager) {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/KnightWarrior.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 300;
        titleFont = generator.generateFont(parameter);
        titleFont.setColor(112/255f, 41/255f, 99/255f, 1);

        text = "Mind Weaver";
        layout = new GlyphLayout(titleFont, text);

        buttons = new Array<>();

        playSkin = new Skin();
        playSkin.add("UnclickedPlayButton", resourceManager.getTexture("UnclickedPlayButton"));
        playSkin.add("ClickedPlayButton", resourceManager.getTexture("ClickedPlayButton"));
        playButton = initButton(playSkin, "UnclickedPlayButton", "ClickedPlayButton", 50, buttonWidth, buttonHeight, Constants.SCREEN_OP.LEVELS);

        settingsSkin = new Skin();
        settingsSkin.add("UnclickedSettingsButton", resourceManager.getTexture("UnclickedSettingsButton"));
        settingsSkin.add("ClickedSettingsButton", resourceManager.getTexture("ClickedSettingsButton"));
        settingsButton = initButton(settingsSkin, "UnclickedSettingsButton", "ClickedSettingsButton",  250, buttonWidth, buttonHeight, Constants.SCREEN_OP.LEVELS);

        exitSkin = new Skin();
        exitSkin.add("UnclickedExitButton", resourceManager.getTexture("UnclickedExitButton"));
        exitSkin.add("ClickedExitButton", resourceManager.getTexture("ClickedExitButton"));

        exitButton = initButton(exitSkin, "UnclickedExitButton", "ClickedExitButton", 450, buttonWidth, buttonHeight, Constants.SCREEN_OP.EXIT);
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
    @Override
    public void dispose() {
        stage.dispose();
        generator.dispose();
        titleFont.dispose();

        for (ImageButton button : buttons) {
            Skin skin = button.getSkin();
            if (skin != null) {
                skin.dispose();
            }
        }
    }
    @Override
    public void show() { }
    @Override
    public void resize(int i, int j) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }

    @Override
    public Matrix4 getProjectionMatrix() {
        return stage.getBatch().getProjectionMatrix();
    }
}
