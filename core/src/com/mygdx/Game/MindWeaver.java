package com.mygdx.Game;

import com.mygdx.Audio.MusicManager;
import com.mygdx.Helpers.Constants;
import com.mygdx.Scenes.HUD;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MindWeaver extends Game {

	public SpriteBatch batch;	//instantiate sprite batch
	public HUD hud;		// HUD holding inventory will remain constant across all screens
	private MyResourceManager resourceManager; //instantiate resource manager

	@Override
	public void create () {
		batch = new SpriteBatch();					//instantiate and initialise batch
		resourceManager = new MyResourceManager();	//initialise resource manager
		//instantiate and initialise screen manager
		ScreenManager screenManager = new ScreenManager(this, resourceManager, new MusicManager());
		loadSprites();

		hud = new HUD(batch, resourceManager);	//heads up display for lives

		screenManager.pushScreen(Constants.SCREEN_OP.START, "none");	//set screen to start screen
	}

	public void loadSprites() {		//load every sprite required for the game

		//Start Screen buttons
		resourceManager.loadTexture("Buttons/UnclickedPlayButton.png", "UnclickedPlayButton");
		resourceManager.loadTexture("Buttons/HoverPlayButton.png", "HoverPlayButton");
		resourceManager.loadTexture("Buttons/ClickedPlayButton.png", "ClickedPlayButton");
		resourceManager.loadTexture("Buttons/UnclickedExitButton.png", "UnclickedExitButton");
		resourceManager.loadTexture("Buttons/HoverExitButton.png", "HoverExitButton");
		resourceManager.loadTexture("Buttons/ClickedExitButton.png", "ClickedExitButton");

		//Menu Screen buttons
		resourceManager.loadTexture("Buttons/UnclickedResumeButton.png", "UnclickedResumeButton");
		resourceManager.loadTexture("Buttons/HoverResumeButton.png", "HoverResumeButton");
		resourceManager.loadTexture("Buttons/ClickedResumeButton.png", "ClickedResumeButton");

		resourceManager.loadTexture("Buttons/UnclickedRestartButton.png", "UnclickedRestartButton");
		resourceManager.loadTexture("Buttons/HoverRestartButton.png", "HoverRestartButton");
		resourceManager.loadTexture("Buttons/ClickedRestartButton.png", "ClickedRestartButton");

		resourceManager.loadTexture("Buttons/UnclickedLevelsButton.png", "UnclickedLevelsButton");
		resourceManager.loadTexture("Buttons/HoverLevelsButton.png", "HoverLevelsButton");
		resourceManager.loadTexture("Buttons/ClickedLevelsButton.png", "ClickedLevelsButton");

		resourceManager.loadTexture("Buttons/UnclickedControlsButton.png", "UnclickedControlsButton");
		resourceManager.loadTexture("Buttons/HoverControlsButton.png", "HoverControlsButton");
		resourceManager.loadTexture("Buttons/ClickedControlsButton.png", "ClickedControlsButton");

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
		resourceManager.loadTexture("Objects/platform.png", "platform");

		//Items
		resourceManager.loadTexture("Items/bug.png", "bug");
		resourceManager.loadTexture("Items/papaya.png", "papaya");

		//Pet
		resourceManager.loadTexture("Items/pet.png", "pet");

		//Merchant
		resourceManager.loadTexture("Merchant/merchant_idle.png", "merchant_idle");

		//HUD
		resourceManager.loadTexture("HUD/life.png", "life");
		resourceManager.loadTexture("HUD/pause.png", "pause");
		resourceManager.loadTexture("HUD/inventory.png", "inventory");

		//Shapes
		resourceManager.loadTexture("Shapes/purple_pixel.png", "purple_pixel");
		resourceManager.loadTexture("Shapes/translucent_pixel.png", "translucent_pixel");
		resourceManager.loadTexture("Shapes/gray_pixel.png", "gray_pixel");

		//Cutscenes
		resourceManager.loadTexture("Cutscenes/cutscene_bg.png", "cutscene_bg");
		resourceManager.loadTexture("Cutscenes/mage_neutral.png", "mage_neutral");
		resourceManager.loadTexture("Cutscenes/mage_chained.png", "mage_chained");
		resourceManager.loadTexture("Cutscenes/mage_freed.png", "mage_freed");
		resourceManager.loadTexture("Cutscenes/mage_happy.png", "mage_happy");
		resourceManager.loadTexture("Cutscenes/mage_pokerface.png", "mage_pokerface");
		resourceManager.loadTexture("Cutscenes/mage_surprised.png", "mage_surprised");
		resourceManager.loadTexture("Cutscenes/merchant_neutral.png", "merchant_neutral");
		resourceManager.loadTexture("Cutscenes/merchant_bling.png", "merchant_bling");
		resourceManager.loadTexture("Cutscenes/butterfly.png", "butterfly");
		resourceManager.loadTexture("Cutscenes/merchant_papaya.png", "merchant_papaya");
		resourceManager.loadTexture("Cutscenes/merchant_papaya3.png", "merchant_papaya3");
		resourceManager.loadTexture("Cutscenes/merchant_smell.png", "merchant_smell");
		resourceManager.loadTexture("Cutscenes/merchant_bug.png", "merchant_bug");
		resourceManager.loadTexture("Cutscenes/mage_eating.png", "mage_eating");
		resourceManager.loadTexture("Cutscenes/mage_ate.png", "mage_ate");
		resourceManager.loadTexture("Cutscenes/bug_teacher.png", "bug_teacher");

		//Messages
		resourceManager.loadTexture("Messages/press_x.png", "x");
		resourceManager.loadTexture("Messages/press_shift.png", "shift");

		//SFX
		resourceManager.loadSound("SoundEffects/jump.mp3", "jump");
		resourceManager.loadSound("SoundEffects/land.mp3", "land");
		resourceManager.loadSound("SoundEffects/laugh.wav", "laugh");
		resourceManager.loadSound("SoundEffects/papaya_picked.wav", "item");
		resourceManager.loadSound("SoundEffects/attack.mp3", "attack");
		resourceManager.loadSound("SoundEffects/lever.mp3", "lever");
		resourceManager.loadSound("SoundEffects/door.mp3", "door");
		resourceManager.loadSound("SoundEffects/cycle.mp3", "cycle");
		resourceManager.loadSound("SoundEffects/level_complete.mp3", "level_complete");

		//Art
		resourceManager.loadTexture("Art/Mind.png", "mind");
		resourceManager.loadTexture("Art/Weaver.png", "weaver");
		resourceManager.loadTexture("Art/LevelsScreen1.png", "LevelsScreen1");
		resourceManager.loadTexture("Art/LevelsScreen2.png", "LevelsScreen2");
		resourceManager.loadTexture("Art/LevelsScreen3.png", "LevelsScreen3");
		resourceManager.loadTexture("Art/LevelsScreen4.png", "LevelsScreen4");
		resourceManager.loadTexture("Art/LevelsScreen5.png", "LevelsScreen5");
		resourceManager.loadTexture("Art/levels_background.png", "levels_bg");
		resourceManager.loadTexture("Art/start_background.png", "start_bg");
		resourceManager.loadTexture("Art/translucent_background.png", "translucent_bg");
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
