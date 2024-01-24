package com.mygdx.Handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.Mage;
import com.mygdx.Tools.Constants.*;

public class MyContactListener implements ContactListener {

    private Fixture fa;
    private Fixture fb;
    private final EntityHandler entityHandler;
    private final PlayerController playerController;

    public MyContactListener(PlayerController playerController, EntityHandler entityHandler) {
        this.playerController = playerController;
        this.entityHandler = entityHandler;
    }

    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            playerController.getCharacter().setWallState(-1);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            playerController.getCharacter().setWallState(1);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            playerController.getCharacter().land();
            if (playerController.getCharacter().getMovementState() == MSTATE.PREV) playerController.getCharacter().setMovementState(MSTATE.HSTILL);
        } else if (fa.getUserData().equals("player_hb") || fb.getUserData().equals("player_hb")) {
            entityHandler.addEntityOperation(playerController.getCharacter(), "die");
        } else if (fa.getUserData().equals("checkpoint") || fb.getUserData().equals("checkpoint")) {
            ((Mage) playerController.getCharacter()).setCheckPoint(fa.getUserData().equals("checkpoint") ? fa.getBody().getPosition() : fb.getBody().getPosition());
        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {
            System.out.println("here");
        }
    }

    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor") || fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            playerController.getCharacter().setWallState(0);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            if (playerController.getCharacter().isInAir()) playerController.getCharacter().removePlayerState(PSTATE.ON_GROUND);
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
