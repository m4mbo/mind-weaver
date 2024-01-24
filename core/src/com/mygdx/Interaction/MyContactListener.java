package com.mygdx.Interaction;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Objects.Mage;
import com.mygdx.Objects.PlayableCharacter;
import com.mygdx.Tools.Constants.*;

public class MyContactListener implements ContactListener {

    private Fixture fa;
    private Fixture fb;
    private final EntityHandler entityHandler;

    public MyContactListener(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
    }

    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character = entityHandler.getCurrCharacter();

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            character.setWallState(-1);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            character.setWallState(1);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            character.land();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);
        } else if (fa.getUserData().equals("player_hb") || fb.getUserData().equals("player_hb")) {
            entityHandler.addEntityOperation(character, "die");
        } else if (fa.getUserData().equals("checkpoint") || fb.getUserData().equals("checkpoint")) {
            ((Mage) character).setCheckPoint(fa.getUserData().equals("checkpoint") ? fa.getBody().getPosition() : fb.getBody().getPosition());
        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {
            System.out.println("here");
        }
    }

    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character = entityHandler.getCurrCharacter();

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor") || fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            character.setWallState(0);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            if (character.isInAir()) character.removePlayerState(PSTATE.ON_GROUND);
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
