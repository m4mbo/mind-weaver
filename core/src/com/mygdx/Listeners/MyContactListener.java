package com.mygdx.Listeners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.Interactable;
import com.mygdx.Objects.Platform;
import com.mygdx.World.EntityHandler;
import com.mygdx.World.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Helpers.Constants.*;
import com.mygdx.Tools.MathWizard;

public class MyContactListener implements ContactListener {

    private Fixture fa;
    private Fixture fb;
    private final EntityHandler entityHandler;
    private final VisionMap visionMap;

    public MyContactListener(EntityHandler entityHandler, VisionMap visionMap) {
        this.entityHandler = entityHandler;
        this.visionMap = visionMap;
    }

    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character;

        if (fa.getUserData() instanceof Interactable || fb.getUserData() instanceof Interactable) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Interactable ? fb.getBody() : fa.getBody());
            character.addInteractable((Interactable) (fa.getUserData() instanceof Interactable ? fa.getUserData() : fb.getUserData()));
        } else if (fa.getUserData() instanceof Platform || fb.getUserData() instanceof Platform) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Platform ? fb.getBody() : fa.getBody());
            character.increaseFloorContact();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);
            Platform platform = (Platform) (fa.getUserData() instanceof Platform ? fa.getUserData() : fb.getUserData());
            platform.addEntityOnTop(character);
        } else if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("leftSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(-1);
        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("rightSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(1);
        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("bottomSensor") ? fa.getBody() : fb.getBody());
            character.increaseFloorContact();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);
        } else if (fa.getUserData().equals("attack") || fb.getUserData().equals("attack")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("attack") ? fb.getBody() : fa.getBody());
            Body characterB2body = character.getB2body();
            character.die();
            // Hit procedure
            Vector2 normalized = MathWizard.normalizedDirection(characterB2body.getPosition(), fa.getUserData().equals("hazard") ? fa.getBody().getPosition() : fb.getBody().getPosition());
            character.stun(0.05f);
            characterB2body.applyLinearImpulse(new Vector2(normalized.x * Constants.KNOCKBACK_SCALE, normalized.y * Constants.KNOCKBACK_SCALE), characterB2body.getWorldCenter(), true);
        } else if (fa.getUserData().equals("spike") || fb.getUserData().equals("spike")) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("spike") ? fb.getBody() : fa.getBody());
            character.setLives(1);
            character.die();
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

        if (fa.getUserData() instanceof Interactable || fb.getUserData() instanceof Interactable) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Interactable ? fb.getBody() : fa.getBody());
            character.removeInteractable((Interactable) (fa.getUserData() instanceof Interactable ? fa.getUserData() : fb.getUserData()));
        } else if (fa.getUserData() instanceof Platform || fb.getUserData() instanceof Platform) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Platform ? fb.getBody() : fa.getBody());
            character.decreaseFloorContact();
            Platform platform = (Platform) (fa.getUserData() instanceof Platform ? fa.getUserData() : fb.getUserData());
            platform.removeEntityOnTop(character);
        } else if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {
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
