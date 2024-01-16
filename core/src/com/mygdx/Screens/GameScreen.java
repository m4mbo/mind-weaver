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
import com.mygdx.Objects.Entity;
import com.mygdx.Objects.Player;
import com.mygdx.Tools.B2WorldCreator;
import com.mygdx.Tools.Constants;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen implements Screen {
    private final MyTimer timer;
    private final Glissoar game;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final OrthogonalTiledMapRenderer renderer;
    private final World world;    // World holding all the physical objects
    private final Box2DDebugRenderer b2dr;
    private final Player player;
    private final LinkedList<Entity> deadEntities;
    private final MyInputProcessor inputProcessor;
    public GameScreen(Glissoar game, String stage, MyResourceManager resourceManager, MyInputProcessor inputProcessor) {

        this.game = game;
        this.inputProcessor = inputProcessor;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen

        AtomicInteger eidAllocator = new AtomicInteger();
        timer = new MyTimer();
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 35 / Constants.PPM, Constants.TILE_SIZE * 19 / Constants.PPM, gameCam);
        TmxMapLoader mapLoader = new TmxMapLoader();
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        TiledMap map = mapLoader.load("test_upgrade.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        world = new World(new Vector2(0, -Constants.G), true);
        player = new Player(200, 100, world, eidAllocator.getAndIncrement(), timer, resourceManager, 3);
        deadEntities = new LinkedList<>();

        inputProcessor.setGameVariables(player, world);
        world.setContactListener(new MyContactListener(player, this));
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);     //Creating world
    }

    @Override
    public void show() {  }

    public void update(float delta) {
        player.update(delta);
        world.step(1/60f, 6, 2);
        handleDeadEntities();    // Handling dead entities after world step to avoid errors
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
        player.render(game.batch);

        //b2dr.render(world, gameCam.combined);
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

    public void addDeadEntity(Entity entity) { deadEntities.add(entity); }

    public void handleDeadEntities() {
        for (Entity entity : deadEntities) {
            entity.die();
        }
        deadEntities.clear();
    }
}
