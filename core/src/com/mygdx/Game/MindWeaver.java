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

		Gdx.input.setInputProcessor(inputMultiplexer);
		setScreen(new GameScreen(this, "everlush", resourceManager, gameInputProcessor));
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
