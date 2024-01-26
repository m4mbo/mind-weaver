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
import com.mygdx.Interaction.MyContactListener;
import com.mygdx.Interaction.MyInputProcessor;
import com.mygdx.Interaction.MyTimer;
import com.mygdx.Objects.BaseGoblin;
import com.mygdx.Objects.Mage;
import com.mygdx.Handlers.B2WorldHandler;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.ShapeDrawer;

import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen implements Screen {
    private final MyTimer timer;
    private final Glissoar game;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final OrthogonalTiledMapRenderer renderer;
    private final World world;    // World holding all the physical objects
    private final Box2DDebugRenderer b2dr;
    private final MyInputProcessor inputProcessor;
    private final EntityHandler entityHandler;
    private final ShapeDrawer shapeDrawer;

    public GameScreen(Glissoar game, String stage, MyResourceManager resourceManager, MyInputProcessor inputProcessor) {

        this.game = game;
        this.inputProcessor = inputProcessor;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen

        // Creating tiled map
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = null;
        if (stage.equals("everlush")) map = mapLoader.load("everlush.tmx");
        else if (stage.equals("verdant_hollow")) map = mapLoader.load("verdant_hollow.tmx");
        else map = mapLoader.load("grim_factory.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        world = new World(new Vector2(0, -Constants.G), true);
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 30 / Constants.PPM, Constants.TILE_SIZE * 17 / Constants.PPM, gameCam);
        gameCam.position.set(2, 77, 0);

        AtomicInteger eidAllocator = new AtomicInteger();
        shapeDrawer = new ShapeDrawer(gameCam);
        timer = new MyTimer();
        entityHandler = new EntityHandler();
        entityHandler.initializeHandler(new Mage(100, 7900, world, eidAllocator.getAndIncrement(), timer, resourceManager, 3, shapeDrawer, entityHandler));
        entityHandler.addEntity(new BaseGoblin(100, 7900, world, eidAllocator.getAndIncrement(), timer, resourceManager, shapeDrawer, entityHandler));

        inputProcessor.setGameVariables(entityHandler, world);
        world.setContactListener(new MyContactListener(entityHandler));
        b2dr = new Box2DDebugRenderer();
        new B2WorldHandler(world, map, resourceManager, timer, eidAllocator);     //Creating world
    }

    @Override
    public void show() {  }

    public void update(float delta) {
        entityHandler.update(delta);
        world.step(1/60f, 6, 2);
        entityHandler.handleEntities();
        gameCam.position.set(entityHandler.getCurrCharacter().getPosition().x, entityHandler.getCurrCharacter().getPosition().y + 40 / Constants.PPM, 0);
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
        entityHandler.render(game.batch);

        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();
        game.batch.end();

        shapeDrawer.render(game.batch);

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
