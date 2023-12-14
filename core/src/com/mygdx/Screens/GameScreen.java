package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Game.Glissoar;
import com.mygdx.Helpers.Constants;

public class GameScreen implements Screen {

    private Glissoar game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture img;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public GameScreen(Glissoar game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, gameCam);
        img = new Texture("badlogic.jpg");
        maploader = new TmxMapLoader();
        map = maploader.load("test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clearing the screen
        Gdx.gl.glClearColor( 1, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(img, 0, 0);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    @Override
    public void dispose() {

    }
}
