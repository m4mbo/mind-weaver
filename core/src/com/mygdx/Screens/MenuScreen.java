package com.mygdx.Screens;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Helpers.Constants;

public class MenuScreen extends ManagedScreen {

    private final ScreenManager screenManager;
    private final float buttonWidth, buttonHeight;
    private final Stage stage;
    private Array<ImageButton> buttons;

    public MenuScreen(MyResourceManager resourceManager, ScreenManager screenManager) {

        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        Gdx.input.setInputProcessor(stage);

        initMenuScreen(resourceManager);
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
                screenManager.pushScreen(screenType, "slide_up");
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

        initButton(resumeSkin, "UnclickedResumeButton", "ClickedResumeButton", -350, buttonWidth, buttonHeight, Constants.SCREEN_OP.RESUME);

        final Skin restartSkin = new Skin();
        restartSkin.add("UnclickedRestartButton", resourceManager.getTexture("UnclickedRestartButton"));
        restartSkin.add("ClickedRestartButton", resourceManager.getTexture("ClickedRestartButton"));

        initButton(restartSkin, "UnclickedRestartButton", "ClickedRestartButton", -150, buttonWidth, buttonHeight, Constants.SCREEN_OP.RESTART);

        final Skin controlsSkin = new Skin();
        controlsSkin.add("UnclickedControlsButton", resourceManager.getTexture("UnclickedControlsButton"));
        controlsSkin.add("ClickedControlsButton", resourceManager.getTexture("ClickedControlsButton"));
        initButton(controlsSkin, "UnclickedControlsButton", "ClickedControlsButton", 50, buttonWidth, buttonHeight, Constants.SCREEN_OP.LEVELS);
        final Skin levelsSkin = new Skin();
        levelsSkin.add("UnclickedLevelsButton", resourceManager.getTexture("UnclickedLevelsButton"));
        levelsSkin.add("ClickedLevelsButton", resourceManager.getTexture("ClickedLevelsButton"));
        initButton(levelsSkin, "UnclickedLevelsButton", "ClickedLevelsButton", 250, buttonWidth, buttonHeight, Constants.SCREEN_OP.LEVELS);

        final Skin exitSkin = new Skin();
        exitSkin.add("UnclickedExitButton", resourceManager.getTexture("UnclickedExitButton"));
        exitSkin.add("ClickedExitButton", resourceManager.getTexture("ClickedExitButton"));
        initButton(exitSkin, "UnclickedExitButton", "ClickedExitButton", 450, buttonWidth, buttonHeight, Constants.SCREEN_OP.EXIT);
    }

    @Override
    public void render(float delta) {
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
    public Matrix4 getProjectionMatrix() {
        return stage.getBatch().getProjectionMatrix();
    }
    public void resume() { }
    @Override
    public void hide() { }

}
