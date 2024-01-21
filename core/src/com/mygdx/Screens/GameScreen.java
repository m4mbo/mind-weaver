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
import com.mygdx.Handlers.*;
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
    private final MyInputProcessor inputProcessor;
    private final EntityHandler entityHandler;
    private Vector2 camNewPos;
    public GameScreen(Glissoar game, String stage, MyResourceManager resourceManager, MyInputProcessor inputProcessor) {

        this.game = game;
        this.inputProcessor = inputProcessor;
        camNewPos = null;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen

        AtomicInteger eidAllocator = new AtomicInteger();
        timer = new MyTimer();
        entityHandler = new EntityHandler();
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 40 / Constants.PPM, Constants.TILE_SIZE * 23 / Constants.PPM, gameCam);
        TmxMapLoader mapLoader = new TmxMapLoader();
        gameCam.position.set(2, 77, 0);

        // Creating tiled map
        TiledMap map = null;
        if (stage.equals("everlush")) map = mapLoader.load("everlush.tmx");
        else if (stage.equals("verdant_hollow")) map = mapLoader.load("verdant_hollow.tmx");
        else map = mapLoader.load("grim_factory.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        world = new World(new Vector2(0, -Constants.G), true);
        player = new Player(100, 7900, world, eidAllocator.getAndIncrement(), timer, resourceManager, 3);

        inputProcessor.setGameVariables(player, world);
        world.setContactListener(new MyContactListener(player, this, entityHandler));
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);     //Creating world
    }

    @Override
    public void show() {  }

    public void update(float delta) {
        player.update(delta);
        world.step(1/60f, 6, 2);
        entityHandler.handleEntities();
        if (camNewPos != null) camStep();
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

        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();
        game.batch.end();
    }

    public void repositionCamera(Vector2 position) {
        camNewPos = position;
    }

    public void camStep() {
        if (comparePosition(gameCam.position.x, camNewPos.x) && comparePosition(gameCam.position.y, camNewPos.y)) {
            gameCam.position.set(camNewPos.x, camNewPos.y, 0);
            camNewPos = null;
            return;
        }
        if (comparePosition(gameCam.position.x, camNewPos.x)) gameCam.translate(0, (gameCam.position.y < camNewPos.y ? 24 : -24) / Constants.PPM, 0);
        else if (comparePosition(gameCam.position.y, camNewPos.y)) gameCam.translate((gameCam.position.x < camNewPos.x ? 24 : -24) / Constants.PPM, 0, 0);
        else gameCam.translate((gameCam.position.x < camNewPos.x ? 8 : -8) / Constants.PPM, (gameCam.position.y < camNewPos.y ? 24 : -24) / Constants.PPM, 0);
    }

    public boolean comparePosition(float pos1, float pos2) {
        // Comparing position with slight offset
        return (pos1 <= (pos2 + (24 / Constants.PPM))) && (pos1 >= (pos2 - (24 / Constants.PPM)));
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
