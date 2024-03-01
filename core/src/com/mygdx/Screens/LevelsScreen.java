package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class LevelsScreen extends ManagedScreen {

    private final Stage stage;
    private final ScreenManager screenManager;
    private final ShapeRenderer shapeRenderer;
    private Array<Polygon> levels;
    private boolean wasClicked;
    private Texture levelsTexture;
    private final int currLevel;

    public LevelsScreen(MyResourceManager resourceManager, ScreenManager screenManager, int levelProgression) {

        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(null);
        currLevel = levelProgression;

        shapeRenderer = new ShapeRenderer();

        initLevelsScreen(resourceManager);

        wasClicked = false;
    }

    //method to initialise levels screen
    public void initLevelsScreen(MyResourceManager resourceManager) {

        //draw background
        Texture bg = resourceManager.getTexture("levels_bg");
        Image levelsBGImage =  new Image(bg);
        levelsBGImage.setPosition(0, 0);
        levelsBGImage.setSize(levelsBGImage.getWidth() * 7.5f, levelsBGImage.getHeight() * 7.5f);
        stage.addActor(levelsBGImage);

        //load relevant texture according to levels completed
        switch (currLevel) {
            case 1:
                levelsTexture = resourceManager.getTexture("LevelsScreen1");
                break;
            case 2:
                levelsTexture = resourceManager.getTexture("LevelsScreen2");
                break;
            case 3:
                levelsTexture = resourceManager.getTexture("LevelsScreen3");
                break;
            case 4:
                levelsTexture = resourceManager.getTexture("LevelsScreen4");
                break;
            case 5:
                levelsTexture = resourceManager.getTexture("LevelsScreen5");
                break;
            default:
                break;
        }

        //convert levels texture to image
        Image levelsImage = new Image(levelsTexture);
        levelsImage.setPosition((float) (Gdx.graphics.getWidth() - levelsTexture.getWidth()) / 2, (float) (Gdx.graphics.getHeight() - levelsTexture.getHeight()) / 2);
        levelsImage.setSize(levelsTexture.getWidth(), levelsTexture.getHeight());
        stage.addActor(levelsImage);

        levels = new Array<>(); //to store the hexagon polygon around the levels platforms for relevant level input
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        //calculate the position of the vertices for the hexagon according to width and height
        float[] levelVertices = {width/4.5f, height/3.3f, width/3.76f, height/2.9f, width/3.76f, height/2.3f, width/4.5f, height/2.1f, width/5.6f, height/2.3f, width/5.6f, height/2.9f};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{width/2.775f, height/2f, width/2.48f, height/1.825f, width/2.48f, height/1.575f, width/2.775f, height/1.475f, width/3.16f, height/1.575f, width/3.16f, height/1.825f};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{width/2.005f, height/3.3f, width/1.85f, height/2.9f, width/1.85f, height/2.3f, width/2.005f, height/2.1f, width/2.2f, height/2.3f, width/2.2f, height/2.9f};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{width/1.575f, height/2f, width/1.475f, height/1.825f, width/1.475f, height/1.575f, width/1.575f, height/1.475f, width/1.69f, height/1.575f, width/1.69f, height/1.825f};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{width/1.292f, height/3.3f, width/1.226f, height/2.9f, width/1.226f, height/2.3f, width/1.292f, height/2.1f, width/1.373f, height/2.3f, width/1.373f, height/2.9f};
        levels.add(new Polygon(levelVertices));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw hexagons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0);
        for (Polygon polygon: levels) {
            shapeRenderer.polygon(polygon.getTransformedVertices());
        }
        shapeRenderer.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();   //draw background image and levels texture

        screenManager.render(delta);

        handleInput();
    }
    public void handleInput() {
        //method that will load the relevant level with a transition only if the previous level was completed
        if (wasClicked) return;
        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touchPoint);

            float touchX = touchPoint.x;
            float touchY = touchPoint.y;

            if ((currLevel>=1) && (Intersector.isPointInPolygon(levels.get(0).getTransformedVertices(), 0, levels.get(0).getVertices().length, touchX, touchY))) {
                wasClicked = true;
                screenManager.pushScreen(Constants.SCREEN_OP.LEVEL_1, "slide_up");
            }
            if ((currLevel>=2) && (Intersector.isPointInPolygon(levels.get(1).getTransformedVertices(), 0, levels.get(1).getVertices().length, touchX, touchY))) {
                wasClicked = true;
                screenManager.pushScreen(Constants.SCREEN_OP.LEVEL_2, "slide_up");
            }
            if ((currLevel>=3) && (Intersector.isPointInPolygon(levels.get(2).getTransformedVertices(), 0, levels.get(2).getVertices().length, touchX, touchY))) {
                wasClicked = true;
                screenManager.pushScreen(Constants.SCREEN_OP.LEVEL_3, "slide_up");
            }
            if ((currLevel>=4) && (Intersector.isPointInPolygon(levels.get(3).getTransformedVertices(), 0, levels.get(3).getVertices().length, touchX, touchY))) {
                wasClicked = true;
                screenManager.pushScreen(Constants.SCREEN_OP.LEVEL_4, "slide_up");
            }
            if ((currLevel>=5) && (Intersector.isPointInPolygon(levels.get(4).getTransformedVertices(), 0, levels.get(4).getVertices().length, touchX, touchY))) {
                wasClicked = true;
                screenManager.pushScreen(Constants.SCREEN_OP.LEVEL_5, "slide_up");
            }
        }
    }
    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
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
    @Override
    public Matrix4 getProjectionMatrix() {
        return stage.getBatch().getProjectionMatrix();
    }

}
