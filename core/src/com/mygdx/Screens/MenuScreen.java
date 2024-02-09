package com.mygdx.Screens;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Game.MindWeaver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {
    private final MindWeaver game;
    private TextButton resumeButton, levelsButton, settingsButton, quitButton;
    private final float buttonWidth, buttonHeight;
    private final MyResourceManager resourceManager;
    private Stage menuStage;
    private final List<TextButton> menuScreenButtons = new ArrayList<>();
    public MenuScreen(MindWeaver game, MyResourceManager resourceManager) {

        menuStage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(menuStage);

        this.game = game;
        this.resourceManager = resourceManager;
        this.buttonWidth = Constants.BUTTON_WIDTH;
        this.buttonHeight = Constants.BUTTON_HEIGHT;

        initMenuScreen(resourceManager);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        menuStage.draw();

    }
    private void initMenuScreen(final MyResourceManager resourceManager) {
        final Skin resumeSkin = new Skin();
        final Skin levelsSkin = new Skin();
        final Skin settingsSkin = new Skin();
        final Skin quitSkin = new Skin();

        resumeSkin.add("UnclickedResumeButton", resourceManager.getTexture("UnclickedResumeButton"));
        resumeSkin.add("ClickedResumeButton", resourceManager.getTexture("ClickedResumeButton"));
        levelsSkin.add("UnclickedLevelsButton", resourceManager.getTexture("UnclickedLevelsButton"));
        levelsSkin.add("ClickedLevelsButton", resourceManager.getTexture("ClickedLevelsButton"));
        settingsSkin.add("UnclickedSettingsButton", resourceManager.getTexture("UnclickedSettingsButton"));
        settingsSkin.add("ClickedSettingsButton", resourceManager.getTexture("ClickedSettingsButton"));
        quitSkin.add("UnclickedQuitButton", resourceManager.getTexture("UnclickedQuitButton"));
        quitSkin.add("ClickedQuitButton", resourceManager.getTexture("ClickedQuitButton"));


        TextButton.TextButtonStyle ResumeButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle LevelsButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle SettingsButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle QuitButtonStyle = new TextButton.TextButtonStyle();

        ResumeButtonStyle.up = resumeSkin.getDrawable("UnclickedResumeButton");
        ResumeButtonStyle.down = resumeSkin.getDrawable("ClickedResumeButton");
        LevelsButtonStyle.up = levelsSkin.getDrawable("UnclickedLevelsButton");
        LevelsButtonStyle.down = levelsSkin.getDrawable("ClickedLevelsButton");
        SettingsButtonStyle.up = settingsSkin.getDrawable("UnclickedSettingsButton");
        SettingsButtonStyle.down = settingsSkin.getDrawable("ClickedSettingsButton");
        QuitButtonStyle.up = quitSkin.getDrawable("UnclickedQuitButton");
        QuitButtonStyle.down = quitSkin.getDrawable("ClickedQuitButton");

        BitmapFont font = new BitmapFont();
        ResumeButtonStyle.font = font;
        LevelsButtonStyle.font = font;
        SettingsButtonStyle.font = font;
        QuitButtonStyle.font = font;

        resumeButton = new TextButton(" ", ResumeButtonStyle);
        resumeButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 + 150);
        resumeButton.setSize(buttonWidth, buttonHeight);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeButton.getStyle().down = resumeSkin.getDrawable("ClickedResumeButton");
                resumeButton.setStyle(resumeButton.getStyle());
                //Gdx.input.setInputProcessor(game.getGameInputProcessor());
                //game.setScreen(new GameScreen(game, "everlush", resourceManager, game.getGameInputProcessor()));
            }
        });

        levelsButton = new TextButton(" ", LevelsButtonStyle);
        levelsButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 - 50);
        levelsButton.setSize(buttonWidth, buttonHeight);
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelsButton.getStyle().down = levelsSkin.getDrawable("ClickedLevelsButton");
                levelsButton.setStyle(levelsButton.getStyle());
                game.setScreen(new LevelsScreen(game, resourceManager));
            }
        });

        settingsButton = new TextButton(" ", SettingsButtonStyle);
        settingsButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 - 250);
        settingsButton.setSize(buttonWidth, buttonHeight);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsButton.getStyle().down = settingsSkin.getDrawable("ClickedSettingsButton");
                settingsButton.setStyle(settingsButton.getStyle());
                game.setScreen(new SettingsScreen(game, resourceManager));
            }
        });

        quitButton = new TextButton(" ", QuitButtonStyle);
        quitButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2, (Gdx.graphics.getHeight() - buttonHeight)/2 - 450);
        quitButton.setSize(buttonWidth, buttonHeight);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitButton.getStyle().down = quitSkin.getDrawable("ClickedQuitButton");
                quitButton.setStyle(quitButton.getStyle());
                game.setScreen(new StartScreen(game, resourceManager));
            }
        });

        menuStage.addActor(resumeButton);
        menuStage.addActor(levelsButton);
        menuStage.addActor(settingsButton);
        menuStage.addActor(quitButton);

    }
    public Stage getMenuStage() {
        return this.menuStage;
    }

    @Override
    public void dispose() {
        this.dispose();
        menuStage.dispose();
        for(TextButton button: menuScreenButtons) {
            button.getSkin().dispose();
        }

    }
    @Override
    public void show() {

    }
    @Override
    public void resize(int i, int j) {

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
