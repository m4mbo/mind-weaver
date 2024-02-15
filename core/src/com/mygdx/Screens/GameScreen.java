package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Graphics.LightManager;
import com.mygdx.Graphics.ParticleHandler;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Tools.*;
import com.mygdx.World.*;
import com.mygdx.Listeners.MyContactListener;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.World.B2WorldHandler;
import com.mygdx.Helpers.Constants;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen extends ManagedScreen {
    private final MyTimer timer;
    private final MindWeaver game;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final OrthogonalTiledMapRenderer renderer;
    private final World world;    // World holding all the physical objects
    private final Box2DDebugRenderer b2dr;
    private final UtilityStation util;
    private final ShapeDrawer shapeDrawer;
    private final GameInputProcessor inputProcessor;
    private final int level;
    private final ScreenManager screenManager;

    public GameScreen(MindWeaver game, int level, MyResourceManager resourceManager, ScreenManager screenManager) {

        this.game = game;
        this.level = level;
        this.screenManager = screenManager;

        // Creating tile map
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = null;

        map = mapLoader.load("Tilemaps/level" + this.level + ".tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        world = new World(new Vector2(0, -Constants.G), true);
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 30 / Constants.PPM, Constants.TILE_SIZE * 17 / Constants.PPM, gameCam);

        ColorGenerator colorGenerator = new ColorGenerator();
        AtomicInteger eidAllocator = new AtomicInteger();

        timer = new MyTimer();

        // Tools and handlers
        ShaderHandler shaderHandler = new ShaderHandler(colorGenerator);
        shapeDrawer = new ShapeDrawer(shaderHandler, resourceManager);
        LightManager lightManager = new LightManager(world);
        ObjectHandler objectHandler = new ObjectHandler();
        VisionMap visionMap =  new VisionMap(world, shapeDrawer, game.hud);
        CharacterCycle characterCycle = new CharacterCycle(visionMap, colorGenerator);
        EntityHandler entityHandler = new EntityHandler(characterCycle, shaderHandler, visionMap);
        ParticleHandler particleHandler = new ParticleHandler();

        inputProcessor = new GameInputProcessor(game, screenManager, characterCycle);
        Gdx.input.setInputProcessor(inputProcessor);

        // Creating station
        util = new UtilityStation(entityHandler, objectHandler, characterCycle, visionMap, particleHandler, shaderHandler, lightManager);

        world.setContactListener(new MyContactListener(util, game.hud, screenManager));
        b2dr = new Box2DDebugRenderer();
        new B2WorldHandler(world, map, resourceManager, timer, eidAllocator, util, level, game.hud);     //Creating world
        lightManager.setDim(0.6f);
    }

    @Override
    public void show() {  }

    public void update(float delta) {
        game.hud.update(delta);
        if (game.hud.standBy()) return;
        util.update(delta, gameCam);
        timer.update(delta);
        world.step(1/60f, 6, 2);
    }

    @Override
    public void render(float delta) {

        update(delta);

        game.batch.setProjectionMatrix(gameCam.combined);

        // Clearing the screen
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(gameCam);
        renderer.render();

        util.render(game.batch, delta);

        shapeDrawer.render(game.batch);

        game.hud.render(game.batch);

        screenManager.render(game.batch, delta);

        //b2dr.render(world, gameCam.combined);

        gameCam.position.set(util.getCharacterCycle().getCurrentCharacter().getPosition().x, util.getCharacterCycle().getCurrentCharacter().getPosition().y + 20 / Constants.PPM, 0);
        gameCam.update();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public void pause() { }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        return gameCam.combined;
    }
}
