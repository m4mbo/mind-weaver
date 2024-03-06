package com.mygdx.Listeners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Objects.Interactable;
import com.mygdx.Objects.Item;
import com.mygdx.Objects.Platform;
import com.mygdx.RoleCast.Merchant;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Scenes.HUD;
import com.mygdx.Screens.ScreenManager;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.UtilityStation;
import com.mygdx.World.EntityHandler;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Helpers.Constants.*;
import com.mygdx.Tools.MathWizard;

public class MyContactListener implements ContactListener {

    // Fixtures partaking in collision
    private Fixture fa;
    private Fixture fb;
    private final UtilityStation util;
    private final HUD hud;
    private final ScreenManager screenManager;
    private final int level;
    private final MyResourceManager resourceManager;

    public MyContactListener(UtilityStation util, HUD hud, ScreenManager screenManager, int level, MyResourceManager resourceManager) {
        this.util = util;
        this.hud = hud;
        this.level = level;
        this.screenManager = screenManager;
        this.resourceManager = resourceManager;
    }

    // Triggers when a fixture collides with a sensor
    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character;

        EntityHandler entityHandler = util.getEntityHandler();

        // If player has collided with a lever
        if (fa.getUserData() instanceof Interactable || fb.getUserData() instanceof Interactable) {
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Interactable ? fb.getBody() : fa.getBody());
            character.addInteractable((Interactable) (fa.getUserData() instanceof Interactable ? fa.getUserData() : fb.getUserData()));

        } else if (fa.getUserData() instanceof Item || fb.getUserData() instanceof Item) {  // Collision with item
            Item item = (Item) (fa.getUserData() instanceof Item ? fa.getUserData() : fb.getUserData());
            util.getObjectHandler().removeObject(item);
            hud.addPapaya(level);

        } else if (fa.getUserData() instanceof Merchant || fb.getUserData() instanceof Merchant) {  // Collision with merchant
            Mage mage = (Mage) entityHandler.getEntity(fa.getUserData() instanceof Merchant ? fb.getBody() : fa.getBody());
            Merchant merchant = (Merchant) (fa.getUserData() instanceof Merchant ? fa.getUserData() : fb.getUserData());
            mage.setMerchantInRange(merchant);

        } else if (fa.getUserData() instanceof Platform || fb.getUserData() instanceof Platform) {  // Player collides with platform
            // Adding entity to platform's entity list to handle horizontal movement
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Platform ? fb.getBody() : fa.getBody());
            character.increaseFloorContact();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);
            Platform platform = (Platform) (fa.getUserData() instanceof Platform ? fa.getUserData() : fb.getUserData());
            platform.addEntityOnTop(character);

        } else if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {    // Collision with wall
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("leftSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(-1);

        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {  // Collision with wall
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("rightSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(1);

        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {    // Collision with ground
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("bottomSensor") ? fa.getBody() : fb.getBody());
            character.increaseFloorContact();
            if (character.getMovementState() == MSTATE.PREV) character.setMovementState(MSTATE.HSTILL);

        } else if (fa.getUserData().equals("attack") || fb.getUserData().equals("attack")) {    // Collision with sword attack
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("attack") ? fb.getBody() : fa.getBody());
            Body characterB2body = character.getB2body();
            character.die();
            // Hit procedure
            Vector2 normalized = MathWizard.normalizedDirection(characterB2body.getPosition(), fa.getUserData().equals("hazard") ? fa.getBody().getPosition() : fb.getBody().getPosition());
            character.stun(0.05f);
            characterB2body.applyLinearImpulse(new Vector2(normalized.x * Constants.KNOCKBACK_SCALE, normalized.y * Constants.KNOCKBACK_SCALE), characterB2body.getWorldCenter(), true);

        } else if (fa.getUserData().equals("spike") || fb.getUserData().equals("spike")) {  // Collision with spike
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("spike") ? fb.getBody() : fa.getBody());
            character.setLives(1);
            character.die();

        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {    // Collision with range of vision
            PlayableCharacter source = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("vision") ? fa.getBody() : fb.getBody());
            PlayableCharacter target = (PlayableCharacter) entityHandler.getEntity((Integer) (fa.getUserData().equals("vision") ? fb : fa).getUserData());
            util.getVisionMap().addTarget(source, target);

        } else if (fa.getUserData().equals("end") || fb.getUserData().equals("end")) {  // Collision with ending sensor
            resourceManager.getSound("level_complete").play(1);
            screenManager.setLevelProgression(level+1);
            screenManager.pushScreen(SCREEN_OP.LEVELS, "sliding_left");
        }
    }

    // Triggered when a fixture stops colliding with a sensor
    @Override
    public void endContact(Contact contact) {

        if (handleFixtures(contact)) return;

        PlayableCharacter character;

        EntityHandler entityHandler = util.getEntityHandler();

        if (fa.getUserData() instanceof Interactable || fb.getUserData() instanceof Interactable) { // Removing lever from player's list
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Interactable ? fb.getBody() : fa.getBody());
            character.removeInteractable((Interactable) (fa.getUserData() instanceof Interactable ? fa.getUserData() : fb.getUserData()));

        } else if (fa.getUserData() instanceof Platform || fb.getUserData() instanceof Platform) {  // Removing entity from platform's list
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData() instanceof Platform ? fb.getBody() : fa.getBody());
            character.decreaseFloorContact();
            Platform platform = (Platform) (fa.getUserData() instanceof Platform ? fa.getUserData() : fb.getUserData());
            platform.removeEntityOnTop(character);

        } else if (fa.getUserData() instanceof Merchant || fb.getUserData() instanceof Merchant) {  // Merchant out of range
            Mage mage = (Mage) entityHandler.getEntity(fa.getUserData() instanceof Merchant ? fb.getBody() : fa.getBody());
            mage.setMerchantInRange(null);

        } else if (fa.getUserData().equals("leftSensor") || fb.getUserData().equals("leftSensor")) {    // Wall out of range
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("leftSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(0);

        } else if (fa.getUserData().equals("rightSensor") || fb.getUserData().equals("rightSensor")) {  // Wall out of range
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("rightSensor") ? fa.getBody() : fb.getBody());
            character.setWallState(0);

        } else if (fa.getUserData().equals("bottomSensor") || fb.getUserData().equals("bottomSensor")) {    // Character on air
            character = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("bottomSensor") ? fa.getBody() : fb.getBody());
            character.decreaseFloorContact();

        } else if (fa.getUserData().equals("vision") || fb.getUserData().equals("vision")) {    // Entity out of range of vision
            PlayableCharacter source = (PlayableCharacter) entityHandler.getEntity(fa.getUserData().equals("vision") ? fa.getBody() : fb.getBody());
            PlayableCharacter target = (PlayableCharacter) entityHandler.getEntity((Integer) (fa.getUserData().equals("vision") ? fb : fa).getUserData());
            util.getVisionMap().removeTarget(source, target);
            if (!util.getVisionMap().traceable(target)) target.looseControl();
        }
    }

    // Method to collect both fixtures
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
