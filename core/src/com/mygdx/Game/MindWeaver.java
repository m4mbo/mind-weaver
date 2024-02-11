package com.mygdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.Listeners.GameInputProcessor;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MindWeaver extends Game {
	public SpriteBatch batch;
	private ScreenManager screenManager;
	private MyResourceManager resourceManager;
	GameInputProcessor gameInputProcessor;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameInputProcessor = new GameInputProcessor(this);
		resourceManager = new MyResourceManager();
		screenManager = new ScreenManager(this, resourceManager, gameInputProcessor);

		// Input chain
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(gameInputProcessor);

		loadSprites();

		Gdx.input.setInputProcessor(inputMultiplexer);
		//setScreen(new GameScreen(this, 1, resourceManager, gameInputProcessor));
		screenManager.setCurrentScreen(ScreenManager.ScreenType.START);
	}
	public void loadSprites() {
		//Start Screen buttons
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedPlayButton.png", "UnclickedPlayButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedPlayButton.png", "ClickedPlayButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedSettingsButton.png", "UnclickedSettingsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedSettingsButton.png", "ClickedSettingsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedExitButton.png", "UnclickedExitButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedExitButton.png", "ClickedExitButton");

		//Menu Screen buttons
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedResumeButton.png", "UnclickedResumeButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedResumeButton.png", "ClickedResumeButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedLevelsButton.png", "UnclickedLevelsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedLevelsButton.png", "ClickedLevelsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedSettingsButton.png", "UnclickedSettingsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedSettingsButton.png", "ClickedSettingsButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/UnclickedQuitButton.png", "UnclickedQuitButton");
		resourceManager.loadTexture("StartAndMenuScreenButtons/ClickedQuitButton.png", "ClickedQuitButton");

		// Mage
		resourceManager.loadTexture("Mage/mage_run.png", "mage_run");
		resourceManager.loadTexture("Mage/mage_idle.png", "mage_idle");
		resourceManager.loadTexture("Mage/mage_jump.png", "mage_jump");
		resourceManager.loadTexture("Mage/mage_land.png", "mage_land");
		resourceManager.loadTexture("Mage/mage_fall.png", "mage_fall");
		resourceManager.loadTexture("Mage/mage_death.png", "mage_death");


		// Base goblin
		resourceManager.loadTexture("Goblins/basegoblin_run.png", "goblin_run");
		resourceManager.loadTexture("Goblins/basegoblin_idle.png", "goblin_idle");
		resourceManager.loadTexture("Goblins/basegoblin_jump.png", "goblin_jump");
		resourceManager.loadTexture("Goblins/basegoblin_land.png", "goblin_land");
		resourceManager.loadTexture("Goblins/basegoblin_fall.png", "goblin_fall");
		resourceManager.loadTexture("Goblins/basegoblin_death.png", "goblin_death");

		// Armoured goblin
		resourceManager.loadTexture("Goblins/armourgoblin_idle.png", "armour_idle");
		resourceManager.loadTexture("Goblins/armourgoblin_run.png", "armour_run");
		resourceManager.loadTexture("Goblins/armourgoblin_land.png", "armour_land");
		resourceManager.loadTexture("Goblins/armourgoblin_fall.png", "armour_fall");
		resourceManager.loadTexture("Goblins/armourgoblin_jump.png", "armour_jump");
		resourceManager.loadTexture("Goblins/armourgoblin_attack.png", "armour_attack");
		resourceManager.loadTexture("Goblins/armourgoblin_death.png", "armour_death");

		// Objects
		resourceManager.loadTexture("Objects/pressureplate_up.png", "pressureplate_up");
		resourceManager.loadTexture("Objects/pressureplate_up2.png", "pressureplate_up2");
		resourceManager.loadTexture("Objects/pressureplate_down.png", "pressureplate_down");
		resourceManager.loadTexture("Objects/pressureplate_down2.png", "pressureplate_down2");
		resourceManager.loadTexture("Objects/door_closed.png", "door_closed");
		resourceManager.loadTexture("Objects/door_closed2.png", "door_closed2");
		resourceManager.loadTexture("Objects/door_open.png", "door_open");
		resourceManager.loadTexture("Objects/door_open2.png", "door_open2");
		resourceManager.loadTexture("Objects/lever_up.png", "lever_up");
		resourceManager.loadTexture("Objects/lever_down.png", "lever_down");

		//Pet
		resourceManager.loadTexture("Shapes/pet.png", "pet");
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
