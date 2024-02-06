package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Graphics.LightManager;
import com.mygdx.Graphics.ParticleHandler;
import com.mygdx.Handlers.CharacterCycle;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MathWizard;
import com.mygdx.Tools.MyResourceManager;

public class Pet extends Entity {

    private final CharacterCycle characterCycle;
    private final float threshold;
    private float angle;
    private ParticleHandler particleHandler;

    private float time;

    public Pet(CharacterCycle characterCycle, World world, float x, float y, int id, MyResourceManager myResourceManager, LightManager lightManager, ParticleHandler particleHandler) {
        super(id, myResourceManager);
        this.characterCycle = characterCycle;
        this.particleHandler = particleHandler;
        threshold = 20 / Constants.PPM;
        angle = 0;
        time = 0;

        setAnimation(TextureRegion.split(resourceManager.getTexture("pet"), 7, 7)[0], 1/4f, false, 0.8f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(0);

        lightManager.addLight(b2body, 80, Constants.BIT_GROUND, new Color(70f/255, 11f/255, 93f/255, 0.8f));

    }

    public void update(float delta) {
        time += delta;
        if (time >= 0.2f) {
            time = 0;
            particleHandler.addParticleEffect("aura", b2body.getPosition().x - 4 / Constants.PPM, b2body.getPosition().y + 8 / Constants.PPM);
        }
        animation.update(delta);
        assess();
    }

    public void assess() {
        PlayableCharacter character = characterCycle.getCurrentCharacter();
        if (compareCoords(character.getPosition().x, b2body.getPosition().x) && compareCoords(character.getPosition().y, b2body.getPosition().y)) {
            b2body.setLinearVelocity(0, 0);
        } else if ((compareCoordsThreshold(character.getPosition().x, b2body.getPosition().x) || compareCoordsThreshold(character.getPosition().y, b2body.getPosition().y))) {
            track();
        }
    }

    public void track() {

        PlayableCharacter character = characterCycle.getCurrentCharacter();

        Vector2 normalized = MathWizard.normalizedDirection(character.getPosition(), b2body.getPosition());

        // Apply the linear impulse to the body
        b2body.setLinearVelocity(new Vector2(normalized.x * Constants.MAX_SPEED_X / 1.2f, normalized.y * Constants.MAX_SPEED_X / 2));

        angle = MathWizard.angle(character.getPosition(), b2body.getPosition());
    }

    public boolean compareCoordsThreshold(float p1, float p2) {
        return (p1 >= p2 + threshold || p1 <= p2 - threshold);
    }

    public boolean compareCoords(float p1, float p2) {
        return (p1 <= p2 + 2 / Constants.PPM && p1 >= p2 - 2 / Constants.PPM);
    }

}
