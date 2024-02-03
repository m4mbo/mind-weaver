package com.mygdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Listeners.StartInputProcessor;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MindWeaver extends Game {
	public SpriteBatch batch;
	private MyResourceManager resourceManager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		resourceManager = new MyResourceManager();

		// Input chain
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GameInputProcessor gameInputProcessor = new GameInputProcessor(this);
		StartInputProcessor startInputProcessor = new StartInputProcessor();
		inputMultiplexer.addProcessor(gameInputProcessor);
		inputMultiplexer.addProcessor(startInputProcessor);

		loadSprites();

		Gdx.input.setInputProcessor(inputMultiplexer);
		setScreen(new GameScreen(this, 1, resourceManager, gameInputProcessor));
	}

	public void loadSprites() {
		// Mage
		resourceManager.loadTexture("Mage/mage_run.png", "mage_run");
		resourceManager.loadTexture("Mage/mage_idle.png", "mage_idle");
		resourceManager.loadTexture("Mage/mage_jump.png", "mage_jump");
		resourceManager.loadTexture("Mage/mage_land.png", "mage_land");
		resourceManager.loadTexture("Mage/mage_fall.png", "mage_fall");

		// Base goblin
		resourceManager.loadTexture("Goblins/basegoblin_run.png", "goblin_run");
		resourceManager.loadTexture("Goblins/basegoblin_idle.png", "goblin_idle");
		resourceManager.loadTexture("Goblins/basegoblin_jump.png", "goblin_jump");
		resourceManager.loadTexture("Goblins/basegoblin_land.png", "goblin_land");
		resourceManager.loadTexture("Goblins/basegoblin_fall.png", "goblin_fall");

		// Armoured goblin
		resourceManager.loadTexture("Goblins/armourgoblin_idle.png", "armour_idle");

		// Objects
		resourceManager.loadTexture("Objects/pressureplate_up.png", "pressureplate_up");
		resourceManager.loadTexture("Objects/pressureplate_down.png", "pressureplate_down");
		resourceManager.loadTexture("Objects/door_closed.png", "door_closed");
		resourceManager.loadTexture("Objects/door_open.png", "door_open");
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
