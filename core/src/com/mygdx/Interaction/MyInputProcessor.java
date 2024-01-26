package com.mygdx.Interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Game.Glissoar;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Objects.PlayableCharacter;
import com.mygdx.Screens.GameScreen;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;

public class MyInputProcessor implements InputProcessor {

    private EntityHandler entityHandler;
    private World world;
    private final Glissoar game;
    public MyInputProcessor(Glissoar game) {
        this.game = game;
    }

    // Function called only by the game screen
    public void setGameVariables(EntityHandler entityHandler, World world) {
        this.entityHandler = entityHandler;
        this.world = world;
    }

    @Override
    public boolean keyDown (int keycode) {
        if (entityHandler.getCurrCharacter() == null || world == null) return false;
        if (game.getScreen() instanceof GameScreen) return keyDownGameScreen(keycode);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        if (entityHandler.getCurrCharacter() == null || world == null) return false;
        if (game.getScreen() instanceof GameScreen) return keyUpGameScreen(keycode);
        return false;
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

    // Helper function to simply keyDown implementation
    public boolean keyDownGameScreen(int keycode) {

        PlayableCharacter character = entityHandler.getCurrCharacter();
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
                if (character.isStateActive(PSTATE.EOT)) entityHandler.setCurrCharacter(character.getTarget());
                else entityHandler.characterRollback();
                character.looseControl();
            default:
                break;
        }
        return false;
    }

    public boolean keyUpGameScreen(int keycode) {
        PlayableCharacter character = entityHandler.getCurrCharacter();
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
        return false;
    }
}


