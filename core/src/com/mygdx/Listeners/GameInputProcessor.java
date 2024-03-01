package com.mygdx.Listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.Game.MindWeaver;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Screens.ScreenManager;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.World.CharacterCycle;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Screens.GameScreen;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.Constants.*;

// Game screen input processor
public class GameInputProcessor implements InputProcessor {
    
    private final CharacterCycle characterCycle;
    private final MindWeaver game;
    private final ScreenManager screenManager;
    private final MyResourceManager resourceManager;

    public GameInputProcessor(MindWeaver game, ScreenManager screenManager, CharacterCycle characterCycle, MyResourceManager resourceManager) {
        this.game = game;
        this.screenManager = screenManager;
        this.characterCycle = characterCycle;
        this.resourceManager = resourceManager;
    }

    // When a key is pressed
    @Override
    public boolean keyDown (int keycode) {

        // Input multiplexing
        if (!(game.getScreen() instanceof GameScreen)) return false;

        // Getting the character being controlled
        PlayableCharacter character = characterCycle.getCurrentCharacter();

        if (character.isStateActive(PSTATE.DYING)) return true;

        // Allowing only cutscene skipping
        if (game.hud.getCurrCutscene() != null) {
            character.setMovementState(MSTATE.HSTILL);
            if (keycode == Input.Keys.X) game.hud.cycleCutscene();
            return true;
        }

        // Handling inventory in the screen
        if (game.hud.standBy()) {
            if (keycode == Input.Keys.I) game.hud.removeInventory();
            return true;
        }

        switch (keycode) {
            case Input.Keys.SPACE:
                // Jumping and wall jumping
                if (character.isStateActive(PSTATE.ON_GROUND)) {
                    character.jump();
                    break;
                }
                if (character.getWallState() != 0) {
                    character.setMovementState(Constants.MSTATE.PREV);
                    character.wallJump();
                    break;
                }
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                // Moving right
                character.setMovementState(Constants.MSTATE.RIGHT);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                // Moving left
                character.setMovementState(Constants.MSTATE.LEFT);
                break;
            case Input.Keys.SHIFT_LEFT:
                // Cycling characters
                if (characterCycle.cycleNext()) resourceManager.getSound("cycle").play(0.5f);
                // Making the previous character loose control
                character.looseControl();
                if (Gdx.input.isKeyPressed(Input.Keys.A)) characterCycle.getCurrentCharacter().setMovementState(Constants.MSTATE.LEFT);
                else if (Gdx.input.isKeyPressed(Input.Keys.D)) characterCycle.getCurrentCharacter().setMovementState(Constants.MSTATE.RIGHT);
                break;
            case Input.Keys.J:
                // Attacking
                if (character instanceof ArmourGoblin) ((ArmourGoblin) character).attack();
                break;
            case Input.Keys.X:
                // Interacting
                if (character instanceof Mage && ((Mage) character).getMerchantInRange() != null) {
                    ((Mage) character).getMerchantInRange().interact();
                } else {
                    character.interact();
                }
                break;
            case Input.Keys.I:
                // Inventory
                character.setMovementState(MSTATE.HSTILL);
                game.hud.pushInventory();
                break;
            case Input.Keys.ESCAPE:
                // Menu
                screenManager.pushScreen(Constants.SCREEN_OP.MENU, "none");
                break;
            default:
                break;
        }
        return true;
    }

    // When a key is released
    @Override
    public boolean keyUp (int keycode) {

        if (!(game.getScreen() instanceof GameScreen)) return false;

        PlayableCharacter character = characterCycle.getCurrentCharacter();

        if (game.hud.standBy() || character.isStateActive(PSTATE.DYING) || game.hud.getCurrCutscene() != null) return true;

        switch (keycode) {
            case Input.Keys.SPACE:
                // Increasing gravity, makes movement smoother
                if (character.isStateActive(PSTATE.ON_GROUND) || character.isStateActive(PSTATE.STUNNED)) break;
                character.fall();
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                // Stopping and immediately checking for input
                if (Gdx.input.isKeyPressed(Input.Keys.A)) character.setMovementState(Constants.MSTATE.LEFT);
                else character.setMovementState(Constants.MSTATE.HSTILL);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                // Stopping and immediately checking for input
                if (Gdx.input.isKeyPressed(Input.Keys.D)) character.setMovementState(Constants.MSTATE.RIGHT);
                else character.setMovementState(Constants.MSTATE.HSTILL);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }
    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        return false;
    }

}


