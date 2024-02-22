package com.mygdx.Screens;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private final MyResourceManager resourceManager;
    private final float buttonWidth, buttonHeight;
    private final Stage stage;
    private Array<ImageButton> buttons;

    public MenuScreen(MyResourceManager resourceManager, ScreenManager screenManager, Texture background) {

        this.screenManager = screenManager;
        this.resourceManager = resourceManager;
        this.stage = new Stage(new ScreenViewport());
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        Gdx.input.setInputProcessor(stage);

        Image levelsBGImage =  new Image(background);
        levelsBGImage.setPosition(0, levelsBGImage.getHeight());
        levelsBGImage.setSize(levelsBGImage.getWidth(), -levelsBGImage.getHeight());
        stage.addActor(levelsBGImage);

        Image transBGImage =  new Image(resourceManager.getTexture("translucent_bg"));
        transBGImage.setPosition(0,0);
        transBGImage.setSize(transBGImage.getWidth() * 7.5f, transBGImage.getHeight() * 7.5f);
        stage.addActor(transBGImage);

        initMenuScreen();

    }

    public void initButton(final String unclickedImagePath, final String hoverImagePath, final String clickedImagePath, int offset, final float width, final float height, final Constants.SCREEN_OP screenType) {
        Skin skin = new Skin();
        skin.add(unclickedImagePath, resourceManager.getTexture(unclickedImagePath));
        skin.add(hoverImagePath, resourceManager.getTexture(hoverImagePath));
        skin.add(clickedImagePath, resourceManager.getTexture(clickedImagePath));

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = skin.getDrawable(unclickedImagePath);
        buttonStyle.imageOver = skin.getDrawable(hoverImagePath);
        buttonStyle.imageDown = skin.getDrawable(clickedImagePath);

        final ImageButton button = new ImageButton(buttonStyle);
        button.setPosition((Gdx.graphics.getWidth() - button.getWidth())/2 , (Gdx.graphics.getHeight() - button.getHeight())/2 - offset);
        button.getImageCell().size(width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.setChecked(false);
                screenManager.pushScreen(screenType, screenType == Constants.SCREEN_OP.RESUME || screenType == Constants.SCREEN_OP.RESTART || screenType == Constants.SCREEN_OP.CONTROLS ? "none" : "slide_down");
            }
        });
        buttons.add(button);
        stage.addActor(button);

    }

    private void initMenuScreen() {

        buttons = new Array<>();

        initButton("UnclickedResumeButton", "HoverResumeButton", "ClickedResumeButton", -350, buttonWidth, buttonHeight, Constants.SCREEN_OP.RESUME);

        initButton("UnclickedRestartButton", "HoverRestartButton","ClickedRestartButton", -150, buttonWidth, buttonHeight, Constants.SCREEN_OP.RESTART);

        initButton("UnclickedControlsButton", "HoverControlsButton","ClickedControlsButton", 50, buttonWidth, buttonHeight, Constants.SCREEN_OP.CONTROLS);

        initButton("UnclickedLevelsButton", "HoverLevelsButton","ClickedLevelsButton", 250, buttonWidth, buttonHeight, Constants.SCREEN_OP.LEVELS);

        initButton("UnclickedExitButton", "HoverExitButton","ClickedExitButton", 450, buttonWidth, buttonHeight, Constants.SCREEN_OP.EXIT);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        screenManager.render(delta);
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
