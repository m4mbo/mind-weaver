package com.mygdx.Listeners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Handlers.EntityHandler;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.Mage;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Helpers.Constants.*;

public class MyContactListener implements ContactListener {

    private Fixture fa;
    private Fixture fb;
    private final EntityHandler entityHandler;
    private final VisionMap visionMap;
    private final CharacterCycle characterCycle;

    public MyContactListener(EntityHandler entityHandler, VisionMap visionMap, CharacterCycle characterCycle) {
        this.entityHandler = entityHandler;
        this.visionMap = visionMap;
        this.characterCycle = characterCycle;
    }

    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("leftSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(-1);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("rightSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(1);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("bottomSensor") ? fa.getBody() : fb.getBody());
            character.increaseFloorContact();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);
        } else if (fa.getUserData().equals("hazard") || fb.getUserData().equals("hazard")) {

            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("hazard") ? fb.getBody() : fa.getBody());
            entityHandler.addEntityOperation(character, "die");

            Body targetBody = fa.getUserData().equals("hazard") ? fb.getBody(): fa.getBody();
            Vector2 targetPos = targetBody.getPosition();
            Vector2 sourcePos = fa.getUserData().equals("hazard") ? fa.getBody().getPosition() : fb.getBody().getPosition();

            float directionX = targetPos.x - sourcePos.x;
            float directionY = targetPos.y - sourcePos.y;

            // Normalizing the direction
            float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
            if (length != 0) {
                directionX /= length;
                directionY /= length;
            }
            character.stun(0.05f);
            targetBody.applyLinearImpulse(new Vector2(directionX * Constants.KNOCKBACK_SCALE, directionY * Constants.KNOCKBACK_SCALE), targetBody.getWorldCenter(), true);

        } else if (fa.getUserData().equals("checkpoint") || fb.getUserData().equals("checkpoint")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("checkpoint") ? fb.getBody() : fa.getBody());
            ((Mage) character).setCheckPoint(fa.getUserData().equals("checkpoint") ? fb.getBody().getPosition() : fa.getBody().getPosition());
        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {

            PlayableCharacter source = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("vision") ? fa.getBody() : fb.getBody());
            PlayableCharacter target = (PlayableCharacter) entityHandler.getEntity((Integer) (fa.getUserData().equals("vision") ? fb : fa).getUserData());

            visionMap.addTarget(source, target);
        }
    }

    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character;

        if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("leftSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(0);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("rightSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(0);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("bottomSensor") ? fa.getBody() : fb.getBody());
            character.decreaseFloorContact();
        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {

            PlayableCharacter source = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("vision") ? fa.getBody() : fb.getBody());
            PlayableCharacter target = (PlayableCharacter) entityHandler.getEntity((Integer) (fa.getUserData().equals("vision") ? fb : fa).getUserData());

            visionMap.removeTarget(source, target);
            if (source.getBullseye() != null && source.getBullseye().equals(target)) source.setBullseye(null);

            if (!visionMap.traceable(target)) target.looseControl();

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
