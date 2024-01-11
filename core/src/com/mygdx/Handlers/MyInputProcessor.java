package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Objects.Player;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;

public class MyInputProcessor implements InputProcessor {

    private Player player;
    private World world;
    public MyInputProcessor(Player player, World world) {
        this.player = player;
        this.world = world;
    }
    @Override
    public boolean keyDown (int keycode) {

        switch (keycode) {
            case Input.Keys.SPACE:
                if (player.isStateActive(PSTATE.ON_GROUND) && !player.isStateActive(PSTATE.WALL_GRABBED)) {
                    player.jump();
                    break;
                }
                if (player.getWallState() != 0) {
                    player.setMovementState(Constants.MSTATE.PREV);
                    player.wallJump();
                    break;
                }
                if (player.isFalling()) player.glide();
                break;
            case Input.Keys.D:
                if (!player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.RIGHT);
                break;
            case Input.Keys.A:
                if (!player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.LEFT);
                break;
            case Input.Keys.W:
                if (player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.UP);
                break;
            case Input.Keys.S:
                if (player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.DOWN);
                break;
            case Input.Keys.CAPS_LOCK:
                if (!player.isStateActive(PSTATE.DASH_CONSUMED)) player.dash();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {

        switch (keycode) {
            case Input.Keys.SPACE:
                if (player.isStateActive(PSTATE.ON_GROUND) || player.isStateActive(PSTATE.DASHING) || player.isStateActive(PSTATE.STUNNED)) break;
                player.fall();
                if (player.isStateActive(PSTATE.GLIDE_CONSUMED)) player.removePlayerState(PSTATE.GLIDING);
                break;
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A) && !player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.LEFT);
                else player.setMovementState(Constants.MSTATE.HSTILL);
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D) && !player.isStateActive(PSTATE.WALL_GRABBED)) player.setMovementState(Constants.MSTATE.RIGHT);
                else player.setMovementState(Constants.MSTATE.HSTILL);
                break;
            case Input.Keys.J:
                if (!player.isStateActive(PSTATE.WALL_GRABBED)) break;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) player.setMovementState(Constants.MSTATE.RIGHT);
                if (Gdx.input.isKeyPressed(Input.Keys.A)) player.setMovementState(Constants.MSTATE.LEFT);
                player.letGo();
                break;
            case Input.Keys.W:
                if (!player.isStateActive(PSTATE.WALL_GRABBED)) break;
                if (Gdx.input.isKeyPressed(Input.Keys.S)) player.setMovementState(Constants.MSTATE.DOWN);
                else player.setMovementState(Constants.MSTATE.FSTILL);
                break;
            case Input.Keys.S:
                if (!player.isStateActive(PSTATE.WALL_GRABBED)) break;
                if (Gdx.input.isKeyPressed(Input.Keys.W)) player.setMovementState(Constants.MSTATE.UP);
                else player.setMovementState(Constants.MSTATE.FSTILL);
                break;
            default:
                break;
        }
        return false;
    }

    // Input polling for a smoother experience
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            if (player.getWallState() != 0) player.grab();
        }
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

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }
}

