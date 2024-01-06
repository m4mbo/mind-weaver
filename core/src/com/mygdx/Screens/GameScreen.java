package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.Game.Glissoar;
import com.mygdx.Handlers.MyContactListener;
import com.mygdx.Handlers.MyInputProcessor;
import com.mygdx.Interfaces.Publisher;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.B2WorldCreator;
import com.mygdx.Tools.Constants;
import com.mygdx.Objects.Player;
import java.util.LinkedList;

public class GameScreen implements Screen, Publisher {

    private Glissoar game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;    // World holding all the physical objects
    private Box2DDebugRenderer b2dr;
    private Player player;
    private LinkedList<float[]> timers;
    private LinkedList<Constants.TIMER_FLAG> timerFlags;
    public GameScreen(Glissoar game) {
        this.game = game;
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen
        timers = new LinkedList<>();
        timerFlags = new LinkedList<>();
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 25 / Constants.PPM, Constants.TILE_SIZE * 14 / Constants.PPM, gameCam);
        maploader = new TmxMapLoader();
        map = maploader.load("test_upgrade.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -Constants.G), true);
        player = new Player(100, 100, world);
        Gdx.input.setInputProcessor(new MyInputProcessor(player));
        world.setContactListener(new MyContactListener(player));
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);     //Creating world
    }

    @Override
    public void show() {
    }

    public void update(float delta) {
        player.update();
        world.step(1/60f, 6, 2);
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {

        update(delta);

        // Clearing the screen
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(gameCam);
        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.end();

        if (!timers.isEmpty()) {
            for (int i = 0; i < timers.size(); i++) {
                float[] timerPair = timers.get(i);
                timerPair[0] += Gdx.graphics.getDeltaTime();
                if (timerPair[0] >= timerPair[1]) {
                    endTimer(i);
                }
            }
        }
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

    public void startTimer(float seconds, Constants.TIMER_FLAG flag) {
        float[] pair = new float[2];
        pair[1] = seconds;
        timers.add(pair);
        timerFlags.add(flag);
    }

    public void endTimer(int index) {
        timers.remove(index);
        Constants.TIMER_FLAG flag = timerFlags.remove(index);

    }

    @Override
    public void addSubscriber(Subscriber subscriber) {

    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {

    }

    @Override
    public void notifySubscribers() {

    }
}
