package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.Game.Glissoar;
import com.mygdx.Helpers.Constants;
import com.mygdx.Objects.Player;
import jdk.javadoc.internal.doclets.formats.html.markup.BodyContents;

public class GameScreen implements Screen {

    private Glissoar game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;    // World holding all the physical objects
    private Box2DDebugRenderer b2dr;
    private Player player;
    public GameScreen(Glissoar game) {
        this.game = game;
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());      // Full-screen
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Constants.TILE_SIZE * 25 / Constants.PPM, Constants.TILE_SIZE * 14 / Constants.PPM, gameCam);
        maploader = new TmxMapLoader();
        map = maploader.load("test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        player = new Player(100, 100, world);
        BodyDef bdef  = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create ground bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }


    @Override
    public void show() {
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) player.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.D) && !player.movingRight()) player.moveRight();
        if (Gdx.input.isKeyPressed(Input.Keys.A) && !player.movingLeft()) player.moveLeft();
    }
    public void update(float dt) {
        handleInput(dt);
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
