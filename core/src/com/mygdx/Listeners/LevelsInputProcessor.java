package com.mygdx.Listeners;

import com.badlogic.gdx.InputProcessor;
import com.mygdx.Game.MindWeaver;
import com.mygdx.Screens.ScreenManager;
import com.mygdx.Tools.MyResourceManager;

public class LevelsInputProcessor implements InputProcessor {
    private final MindWeaver game;
    private final MyResourceManager resourceManager;
    private final ScreenManager screenManager;

    public LevelsInputProcessor(MindWeaver game, MyResourceManager resourceManager, ScreenManager screenManager) {
        this.game = game;
        this.resourceManager = resourceManager;
        this.screenManager =screenManager;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
