package com.mygdx.Handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.Player;
import com.mygdx.Screens.GameScreen;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;

public class MyContactListener implements ContactListener {

    private Fixture fa;
    private Fixture fb;
    private final Player player;
    private final GameScreen screen;

    public MyContactListener(Player player, GameScreen screen) {
        this.player = player;
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            player.setWallState(-1);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            player.setWallState(1);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            player.land();
            if (player.getMovementState() == MSTATE.PREV) player.setMovementState(MSTATE.HSTILL);
        } else if (fa.getUserData().equals("player_hb") || fb.getUserData().equals("player_hb")) {
            screen.addDeadEntity(player);
        }
    }

    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor") || fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            player.setWallState(0);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            player.removePlayerState(PSTATE.ON_GROUND);
        }
    }

    public boolean handleFixtures(Contact contact) {
        fa = contact.getFixtureA();
        fb = contact.getFixtureB();

        if (fa == null || fb == null) {
            return true;
        }
        return false;
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
