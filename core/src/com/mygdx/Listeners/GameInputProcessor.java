package com.mygdx.Listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Screens.GameScreen;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.Constants.*;

public class GameInputProcessor implements InputProcessor {

    private CharacterCycle characterCycle;
    private final MindWeaver game;
    public GameInputProcessor(MindWeaver game) {
        this.game = game;
    }

    // Function called only by the game screen
    public void setGameVariables(CharacterCycle characterCycle) {
        this.characterCycle = characterCycle;
    }

    @Override
    public boolean keyDown (int keycode) {

        if (!(game.getScreen() instanceof GameScreen)) return false;

        PlayableCharacter character = characterCycle.getCurrentCharacter();
        switch (keycode) {
            case Input.Keys.SPACE:
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
            case Input.Keys.D:
                character.setMovementState(Constants.MSTATE.RIGHT);
                break;
            case Input.Keys.A:
                character.setMovementState(Constants.MSTATE.LEFT);
                break;
            case Input.Keys.SHIFT_LEFT:
                characterCycle.cycleNext();
                character.looseControl();
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {

        if (!(game.getScreen() instanceof GameScreen)) return false;

        PlayableCharacter character = characterCycle.getCurrentCharacter();
        switch (keycode) {
            case Input.Keys.SPACE:
                if (character.isStateActive(PSTATE.ON_GROUND) || character.isStateActive(PSTATE.STUNNED)) break;
                character.fall();
                break;
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) character.setMovementState(Constants.MSTATE.LEFT);
                else character.setMovementState(Constants.MSTATE.HSTILL);
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D)) character.setMovementState(Constants.MSTATE.RIGHT);
                else character.setMovementState(Constants.MSTATE.HSTILL);
                break;
            default:
                break;
        }
        return true;
    }

    /*
     * Input polling for a smoother experience
     */
    public void update() { }

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


