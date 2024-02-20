package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;

public class LevelsScreen extends ManagedScreen {

    private Stage stage;
    private final MyResourceManager resourceManager;
    private final ScreenManager screenManager;
    private Texture levelsTexture;
    private ShapeRenderer shapeRenderer;
    private Array<Polygon> levels;
    public LevelsScreen(MyResourceManager resourceManager, ScreenManager screenManager) {

        this.screenManager = screenManager;
        this.resourceManager = resourceManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(null);

        shapeRenderer = new ShapeRenderer();

        initLevelsScreen(resourceManager);
    }

    public void initLevelsScreen(MyResourceManager resourceManager) {

        levelsTexture = resourceManager.getTexture("LevelsScreen");

        Image levelsImage = new Image(levelsTexture);
        levelsImage.setPosition((Gdx.graphics.getWidth() - levelsTexture.getWidth()) / 2, (Gdx.graphics.getHeight() - levelsTexture.getHeight()) / 2);
        levelsImage.setSize(levelsTexture.getWidth(), levelsTexture.getHeight());
        stage.addActor(levelsImage);

        levels = new Array<>();

        float[] levelVertices = {425, 365, 510, 410, 510, 515, 425, 575, 344, 515, 344, 410};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{690, 605, 774, 650, 774, 755, 690, 815, 608, 755, 608, 650};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{955, 365, 1038, 410, 1038, 515, 955, 575, 872, 515, 872, 410};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{1220, 605, 1302, 650, 1302, 755, 1220, 815, 1136, 755, 1136, 650};
        levels.add(new Polygon(levelVertices));

        levelVertices = new float[]{1485, 365, 1566, 410, 1566, 515, 1485, 575, 1400, 515, 1400, 410};
        levels.add(new Polygon(levelVertices));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (Polygon polygon: levels) {
            shapeRenderer.polygon(polygon.getTransformedVertices());
        }
        shapeRenderer.end();

        handleInput();
    }
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touchPoint);

            float touchX = touchPoint.x;
            float touchY = touchPoint.y;

            if (Intersector.isPointInPolygon(levels.get(0).getTransformedVertices(), 0, levels.get(0).getVertices().length, touchX, touchY)) {
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_1);
            }
            if (Intersector.isPointInPolygon(levels.get(1).getTransformedVertices(), 0, levels.get(1).getVertices().length, touchX, touchY)) {
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_2);
            }
            if (Intersector.isPointInPolygon(levels.get(2).getTransformedVertices(), 0, levels.get(2).getVertices().length, touchX, touchY)) {
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_3);
            }
            if (Intersector.isPointInPolygon(levels.get(3).getTransformedVertices(), 0, levels.get(3).getVertices().length, touchX, touchY)) {
                screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_4);
            }
            if (Intersector.isPointInPolygon(levels.get(4).getTransformedVertices(), 0, levels.get(4).getVertices().length, touchX, touchY)) {
                //screenManager.pushScreen(Constants.SCREEN_TYPE.LEVEL_5);
            }
        }
    }
    public void update(float delta) {
        handleInput();
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
