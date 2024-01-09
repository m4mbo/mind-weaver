package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Game.Glissoar;
import com.mygdx.Handlers.MyContactListener;
import com.mygdx.Handlers.MyInputProcessor;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Handlers.MyTimer;
import com.mygdx.Objects.Player;
import com.mygdx.Tools.B2WorldCreator;
import com.mygdx.Tools.Constants;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen implements Screen {
    private String stage;
    private MyTimer timer;
    private Glissoar game;
    private static AtomicInteger eidAllocator;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;    // World holding all the physical objects
    private Box2DDebugRenderer b2dr;
    private Player player;
    private MyInputProcessor inputProcessor;
    public GameScreen(Glissoar game, String stage, MyResourceManager resourceManager) {
        this.stage = stage;
        this.game = game;
        eidAllocator = new AtomicInteger();
        timer = new MyTimer();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 25 / Constants.PPM, Constants.TILE_SIZE * 14 / Constants.PPM, gameCam);
        maploader = new TmxMapLoader();
        map = maploader.load("test_upgrade.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -Constants.G), true);
        player = new Player(100, 100, world, eidAllocator.getAndIncrement(), timer, resourceManager);
        inputProcessor = new MyInputProcessor(player, world);
        Gdx.input.setInputProcessor(inputProcessor);
        world.setContactListener(new MyContactListener(player));
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);     //Creating world
    }

    @Override
    public void show() {  }

    public void update(float delta) {
        player.update(delta);
        world.step(1/60f, 6, 2);
        gameCam.update();
        timer.update(delta);
        inputProcessor.update();
    }

    @Override
    public void render(float delta) {

        update(delta);

        // Clearing the screen
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(gameCam);
        renderer.render();
        //player.render(game.batch);

        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);



        game.batch.begin();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
