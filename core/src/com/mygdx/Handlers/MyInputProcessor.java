package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Game.Glissoar;
import com.mygdx.Screens.GameScreen;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;

public class MyInputProcessor implements InputProcessor {

    private PlayerController playerController;
    private World world;
    private final Glissoar game;
    public MyInputProcessor(Glissoar game) {
        this.game = game;
    }

    // Function called only by the game screen
    public void setGameVariables(PlayerController playerController, World world) {
        this.playerController = playerController;
        this.world = world;
    }

    @Override
    public boolean keyDown (int keycode) {
        if (playerController.getCharacter() == null || world == null) return false;
        if (game.getScreen() instanceof GameScreen) return keyDownGameScreen(keycode);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        if (playerController.getCharacter() == null || world == null) return false;
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
        switch (keycode) {
            case Input.Keys.SPACE:
                if (playerController.getCharacter().isStateActive(PSTATE.ON_GROUND)) {
                    playerController.getCharacter().jump();
                    break;
                }
                if (playerController.getCharacter().getWallState() != 0) {
                    playerController.getCharacter().setMovementState(Constants.MSTATE.PREV);
                    playerController.getCharacter().wallJump();
                    break;
                }
                break;
            case Input.Keys.D:
                playerController.getCharacter().setMovementState(Constants.MSTATE.RIGHT);
                break;
            case Input.Keys.A:
                playerController.getCharacter().setMovementState(Constants.MSTATE.LEFT);
                break;
            default:
                break;
        }
        return false;
    }

    public boolean keyUpGameScreen(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                if (playerController.getCharacter().isStateActive(PSTATE.ON_GROUND) || playerController.getCharacter().isStateActive(PSTATE.STUNNED)) break;
                playerController.getCharacter().fall();
                break;
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) playerController.getCharacter().setMovementState(Constants.MSTATE.LEFT);
                else playerController.getCharacter().setMovementState(Constants.MSTATE.HSTILL);
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D)) playerController.getCharacter().setMovementState(Constants.MSTATE.RIGHT);
                else playerController.getCharacter().setMovementState(Constants.MSTATE.HSTILL);
                break;
            default:
                break;
        }
        return false;
    }
}


