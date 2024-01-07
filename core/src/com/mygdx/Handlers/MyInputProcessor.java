package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.Objects.Player;
import com.mygdx.Tools.Constants;

public class MyInputProcessor implements InputProcessor {

    private Player player;
    public MyInputProcessor(Player player) {
        this.player = player;
    }
    @Override
    public boolean keyDown (int keycode) {

        switch (keycode) {
            case Input.Keys.SPACE:
                if (player.isOnGround() && !player.isWallGrabbed()) {
                    player.jump();
                    break;
                }
                if (player.getWallState() != 0) {
                    player.setMovementState(Constants.MFLAG.PREV);
                    player.wallJump();
                    break;
                }
                //player.glide();
                break;
            case Input.Keys.D:
                if (!player.isWallGrabbed()) player.setMovementState(Constants.MFLAG.RIGHT);
                break;
            case Input.Keys.A:
                if (!player.isWallGrabbed()) player.setMovementState(Constants.MFLAG.LEFT);
                break;
            case Input.Keys.J:
                if (player.getWallState() != 0) player.grab();
                break;
            case Input.Keys.W:
                if (player.isWallGrabbed()) player.setMovementState(Constants.MFLAG.UP);
                break;
            case Input.Keys.S:
                if (player.isWallGrabbed()) player.setMovementState(Constants.MFLAG.DOWN);
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
                break;
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) player.setMovementState(Constants.MFLAG.LEFT);
                else player.setMovementState(Constants.MFLAG.HSTILL);
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D)) player.setMovementState(Constants.MFLAG.RIGHT);
                else player.setMovementState(Constants.MFLAG.HSTILL);
                break;
            case Input.Keys.J:
                if (player.isWallGrabbed()) {
                    if (Gdx.input.isKeyPressed(Input.Keys.D)) player.setMovementState(Constants.MFLAG.RIGHT);
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) player.setMovementState(Constants.MFLAG.LEFT);
                    player.letGo();
                }
                break;
            case Input.Keys.W:
                if (!player.isWallGrabbed()) break;
                if (Gdx.input.isKeyPressed(Input.Keys.S)) player.setMovementState(Constants.MFLAG.DOWN);
                else player.setMovementState(Constants.MFLAG.FSTILL);
                break;
            case Input.Keys.S:
                if (!player.isWallGrabbed()) break;
                if (Gdx.input.isKeyPressed(Input.Keys.W)) player.setMovementState(Constants.MFLAG.UP);
                else player.setMovementState(Constants.MFLAG.FSTILL);
                break;
            default:
                break;
        }
        return false;
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

