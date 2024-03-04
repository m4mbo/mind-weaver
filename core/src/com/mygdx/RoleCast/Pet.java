package com.mygdx.RoleCast;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Tools.UtilityStation;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.MathWizard;
import com.mygdx.Tools.MyResourceManager;

public class Pet extends Entity {

    private final float threshold;
    private float angle;
    private final UtilityStation util;
    private float time;

    public Pet(World world, float x, float y, int id, MyResourceManager myResourceManager, UtilityStation util) {
        super(id, myResourceManager);
        this.util = util;

        threshold = 20 / Constants.PPM;
        angle = 0;
        time = 0;

        // Initializing sprite
        setAnimation(TextureRegion.split(resourceManager.getTexture("pet"), 7, 7)[0], 1/4f, false, 0.7f);

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(0);
        b2body.setLinearDamping(5);

        //Load butterfly's light
        util.getLightManager().addPointLight(b2body, 80, Constants.BIT_GROUND, new Color(70f/255, 11f/255, 93f/255, 0.8f));

    }

    public void update(float delta) {
        time += delta;
        if (time >= 0.2f) { //animate butterfly flapping
            time = 0;
            util.getParticleHandler().addParticleEffect("aura", b2body.getPosition().x - 4 / Constants.PPM, b2body.getPosition().y + 8 / Constants.PPM);
        }
        animation.update(delta);
        assess();
    }

    public void assess() {
        PlayableCharacter character = util.getCharacterCycle().getCurrentCharacter();
        if (MathWizard.inRange(character.getPosition(), b2body.getPosition(), 0.1f / Constants.PPM)) {
            b2body.setLinearVelocity(0, 0);
        } else if (!MathWizard.inRange(character.getPosition().x, b2body.getPosition().x, threshold) || !MathWizard.inRange(character.getPosition().y, b2body.getPosition().y, threshold)) {
            step(character);
        }
    }

    public void step(PlayableCharacter character) {

        Vector2 normalized = MathWizard.normalizedDirection(character.getPosition(), b2body.getPosition());

        facingRight = normalized.x > 0;

        // Apply linear velocity
        b2body.setLinearVelocity(new Vector2(normalized.x * Constants.MAX_SPEED_X / 1.1f, normalized.y * Constants.MAX_SPEED_X / 2));

        angle = MathWizard.angle(character.getPosition(), b2body.getPosition());
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setShader(util.getShaderHandler().getShaderProgram("rand_col"));
        batch.draw(animation.getFrame(), facingRight ? b2body.getPosition().x - (width / Constants.PPM) / 2 : b2body.getPosition().x + (width / Constants.PPM) / 2 , b2body.getPosition().y - (height / Constants.PPM) / 2, 0, 0, (facingRight ? width : -width) / Constants.PPM, height / Constants.PPM, 1, 1, angle);
        batch.setShader(null);
        batch.end();
    }
}
