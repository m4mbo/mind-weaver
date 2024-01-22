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
    private final EntityHandler entityHandler;

    public MyContactListener(Player player, GameScreen screen, EntityHandler entityHandler) {
        this.player = player;
        this.screen = screen;
        this.entityHandler = entityHandler;
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
            entityHandler.addEntityOperation(player, "die");
        } else if (fa.getUserData().equals("camera_section") || fb.getUserData().equals("camera_section")) {
            screen.repositionCamera(fa.getUserData().equals("camera_section") ? fa.getBody().getPosition() : fb.getBody().getPosition());
        } else if (fa.getUserData().equals("checkpoint") || fb.getUserData().equals("checkpoint")) {
            player.setCheckPoint(fa.getUserData().equals("checkpoint") ? fa.getBody().getPosition() : fb.getBody().getPosition());
        }
    }

    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor") || fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            if (player.isStateActive(PSTATE.WALL_GRABBED)) {
                if (player.getMovementState() == MSTATE.UP) {
                    entityHandler.addEntityOperation(player, "wallclimb");
                } else {
                    player.letGo();
                }
            }
            player.setWallState(0);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            if (player.isInAir()) player.removePlayerState(PSTATE.ON_GROUND);
        }
    }

    public boolean handleFixtures(Contact contact) {
        fa = contact.getFixtureA();
        fb = contact.getFixtureB();

        return fa == null || fb == null;
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
