package com.mygdx.Game;

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
		setScreen(new GameScreen(this, "Everlush", resourceManager));
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
