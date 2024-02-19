package com.mygdx.Screens;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Helpers.Constants;

public class MenuScreen implements Screen {
    private final ScreenManager screenManager;
    private ImageButton resumeButton, restartButton, settingsButton, levelsButton, exitButton;
    private final float buttonWidth, buttonHeight;
    private Stage stage;
    private Array<ImageButton> buttons;
    public MenuScreen(MyResourceManager resourceManager, ScreenManager screenManager) {

        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        Gdx.input.setInputProcessor(stage);

        initMenuScreen(resourceManager);
    }

    public ImageButton initButton(final Skin skin, final String unclickedImagePath, final String clickedImagePath, int offset, final float width, final float height, final Constants.SCREEN_TYPE screenType) {
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
                screenManager.pushScreen(screenType);
            }
        });
        buttons.add(button);
        stage.addActor(button);

        return button;
    }

    private void initMenuScreen(MyResourceManager resourceManager) {

        buttons = new Array<>();

        final Skin resumeSkin = new Skin();
        resumeSkin.add("UnclickedResumeButton", resourceManager.getTexture("UnclickedResumeButton"));
        resumeSkin.add("ClickedResumeButton", resourceManager.getTexture("ClickedResumeButton"));
        resumeButton = initButton(resumeSkin, "UnclickedResumeButton", "ClickedResumeButton", -350, buttonWidth, buttonHeight, Constants.SCREEN_TYPE.RESUME);

        final Skin restartSkin = new Skin();
        restartSkin.add("UnclickedRestartButton", resourceManager.getTexture("UnclickedRestartButton"));
        restartSkin.add("ClickedRestartButton", resourceManager.getTexture("ClickedRestartButton"));
        restartButton = initButton(restartSkin, "UnclickedRestartButton", "ClickedRestartButton", -150, buttonWidth, buttonHeight, Constants.SCREEN_TYPE.RESTART);

        final Skin settingsSkin = new Skin();
        settingsSkin.add("UnclickedSettingsButton", resourceManager.getTexture("UnclickedSettingsButton"));
        settingsSkin.add("ClickedSettingsButton", resourceManager.getTexture("ClickedSettingsButton"));
        settingsButton = initButton(settingsSkin, "UnclickedSettingsButton", "ClickedSettingsButton", 50, buttonWidth, buttonHeight, Constants.SCREEN_TYPE.SETTINGS);

        final Skin levelsSkin = new Skin();
        levelsSkin.add("UnclickedLevelsButton", resourceManager.getTexture("UnclickedLevelsButton"));
        levelsSkin.add("ClickedLevelsButton", resourceManager.getTexture("ClickedLevelsButton"));
        levelsButton = initButton(levelsSkin, "UnclickedLevelsButton", "ClickedLevelsButton", 250, buttonWidth, buttonHeight, Constants.SCREEN_TYPE.LEVELS);

        final Skin exitSkin = new Skin();
        exitSkin.add("UnclickedExitButton", resourceManager.getTexture("UnclickedExitButton"));
        exitSkin.add("ClickedExitButton", resourceManager.getTexture("ClickedExitButton"));
        exitButton = initButton(exitSkin, "UnclickedExitButton", "ClickedExitButton", 450, buttonWidth, buttonHeight, Constants.SCREEN_TYPE.EXIT);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();

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
}
