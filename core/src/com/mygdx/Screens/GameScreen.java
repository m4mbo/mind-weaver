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
import com.mygdx.Game.MindWeaver;
import com.mygdx.Graphics.LightManager;
import com.mygdx.Graphics.ParticleHandler;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Handlers.*;
import com.mygdx.Listeners.MyContactListener;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Objects.Door;
import com.mygdx.Objects.PressurePlate;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.Pet;
import com.mygdx.Tools.MyTimer;
import com.mygdx.RoleCast.BaseGoblin;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Handlers.B2WorldHandler;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.ShapeDrawer;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen implements Screen {
    private final MyTimer timer;
    private final MindWeaver game;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final OrthogonalTiledMapRenderer renderer;
    private final World world;    // World holding all the physical objects
    private final Box2DDebugRenderer b2dr;
    private final EntityHandler entityHandler;
    private final ObjectHandler objectHandler;
    private final VisionMap visionMap;
    private final CharacterCycle characterCycle;
    private final ShapeDrawer shapeDrawer;
    private final ShaderHandler shaderHandler;
    private final LightManager lightManager;
    private final ParticleHandler particleHandler;
    public GameScreen(MindWeaver game, int level, MyResourceManager resourceManager, GameInputProcessor inputProcessor) {

        this.game = game;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen

        // Creating tile map
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = null;

        switch (level) {
            case 1:
                map = mapLoader.load("Tilemaps/level1.tmx");
                break;
            case 2:
                map = mapLoader.load("Tilemaps/level2.tmx");
                break;
            default:
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        world = new World(new Vector2(0, -Constants.G), true);
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 30 / Constants.PPM, Constants.TILE_SIZE * 17 / Constants.PPM, gameCam);
        gameCam.position.set(2, 77, 0);

        AtomicInteger eidAllocator = new AtomicInteger();
        shaderHandler = new ShaderHandler();
        lightManager = new LightManager(world);
        shapeDrawer = new ShapeDrawer(shaderHandler, game.batch);
        timer = new MyTimer();

        objectHandler = new ObjectHandler();
        Door door = new Door(world, resourceManager, 330, 138);
        objectHandler.addObject(door);
        PressurePlate pressurePlate = new PressurePlate(world, resourceManager, 200, 131, 2);
        pressurePlate.addReactable(door);
        objectHandler.addObject(pressurePlate);
        visionMap =  new VisionMap(world, shapeDrawer);
        characterCycle = new CharacterCycle(visionMap);
        entityHandler = new EntityHandler(characterCycle, shaderHandler);

        particleHandler = new ParticleHandler();

        Mage mage = new Mage(250, 200, world, eidAllocator.getAndIncrement(), timer, resourceManager, 3, characterCycle, visionMap, particleHandler);

        characterCycle.initialize(mage);
        entityHandler.addEntity(mage);

        entityHandler.addEntity(new ArmourGoblin(180, 150, world, eidAllocator.getAndIncrement(), timer, resourceManager, characterCycle,visionMap, particleHandler));
        entityHandler.addEntity(new BaseGoblin(200, 150, world, eidAllocator.getAndIncrement(), timer, resourceManager, characterCycle,visionMap,particleHandler));
        entityHandler.addPet(new Pet(characterCycle, world, 250, 200, eidAllocator.getAndIncrement(), resourceManager, lightManager, particleHandler));
        visionMap.initialize(entityHandler);
        inputProcessor.setGameVariables(characterCycle);

        world.setContactListener(new MyContactListener(entityHandler, visionMap, characterCycle));
        b2dr = new Box2DDebugRenderer();
        new B2WorldHandler(world, map, resourceManager, timer, eidAllocator);     //Creating world
        lightManager.setDim(0.8f);
    }

    @Override
    public void show() {  }

    public void update(float delta) {

        shaderHandler.update(delta);
        entityHandler.update(delta);
        visionMap.update(delta);
        timer.update(delta);
        objectHandler.update(delta);
        lightManager.update(gameCam);

        world.step(1/60f, 6, 2);

        entityHandler.handleEntities();
    }

    @Override
    public void render(float delta) {

        update(delta);

        game.batch.setProjectionMatrix(gameCam.combined);

        // Clearing the screen
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(gameCam);
        renderer.render();

        objectHandler.render(game.batch);
        shapeDrawer.render(game.batch);

        particleHandler.render(game.batch, delta);
        lightManager.render();
        entityHandler.render(game.batch);

        //b2dr.render(world, gameCam.combined);

        gameCam.position.set(characterCycle.getCurrentCharacter().getPosition().x, characterCycle.getCurrentCharacter().getPosition().y + 20 / Constants.PPM, 0);
        gameCam.update();
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
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

}
