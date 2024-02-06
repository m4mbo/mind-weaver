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

        setAnimation(TextureRegion.split(resourceManager.getTexture("pet"), 7, 7)[0], 1/4f, false, 1);

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

        float targetX = character.getPosition().x;
        float targetY = character.getPosition().y;

        float currentX = b2body.getPosition().x;
        float currentY = b2body.getPosition().y;

        float directionX = targetX - currentX;
        float directionY = targetY - currentY;

        // Normalizing the direction
        float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        if (length != 0) {
            directionX /= length;
            directionY /= length;
        }

        // Define the speed or strength of the impulse
        float vel = 1.0f; // You may adjust this value based on your requirements

        // Apply the linear impulse to the body
        b2body.setLinearVelocity(new Vector2(directionX * vel, directionY * vel));

        float slope = (targetY - currentY) / (targetX - currentX);
        angle = (float) (Math.atan(slope) * (180 / Math.PI));
    }

    public boolean compareCoordsThreshold(float p1, float p2) {
        return (p1 >= p2 + threshold || p1 <= p2 - threshold);
    }

    public boolean compareCoords(float p1, float p2) {
        return (p1 <= p2 + 2 / Constants.PPM && p1 >= p2 - 2 / Constants.PPM);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(animation.getFrame(), facingRight ? b2body.getPosition().x - (width / Constants.PPM) / 2 : b2body.getPosition().x + (width / Constants.PPM) / 2 , b2body.getPosition().y - (height / Constants.PPM) / 2, 0, 0, (facingRight ? width : -width) / Constants.PPM, height / Constants.PPM, 1, 1, angle);
        batch.end();
    }
}
