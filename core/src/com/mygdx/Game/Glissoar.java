package com.mygdx.Game;

import com.badlogic.gdx.Gdx;
import com.mygdx.Handlers.MyInputProcessor;
import com.mygdx.Handlers.MyResourceManager;
import com.mygdx.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Glissoar extends Game {
	public SpriteBatch batch;
	private MyResourceManager resourceManager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		resourceManager = new MyResourceManager();
		MyInputProcessor inputProcessor = new MyInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
		setScreen(new GameScreen(this, "Everlush", resourceManager, inputProcessor));
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
