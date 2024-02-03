package com.mygdx.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.Helpers.Constants;

public abstract class B2Sprite {
    protected Body b2body;
    protected Animation animation;
    protected float width;
    protected float height;
    protected boolean facingRight;
    private float resize;

    public B2Sprite() {
        animation = new Animation();
        facingRight = true;
        resize = 1;
    }

    public void setAnimation(TextureRegion[] region, float delay, boolean loopLastFrame, float resize, int currFrame) {
        animation.setFrames(region, delay, loopLastFrame, currFrame);
        width = region[0].getRegionWidth() * resize;
        height = region[0].getRegionHeight() * resize;
        this.resize = resize;
    }

    public void setAnimation(TextureRegion[] region, float delay, boolean loopLastFrame, float resize) {
        animation.setFrames(region, delay, loopLastFrame, 0);
        width = region[0].getRegionWidth() * resize;
        height = region[0].getRegionHeight() * resize;
        this.resize = resize;
    }

    public void handleAnimation() { }

    public void update(float delta) {
        animation.update(delta);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(animation.getFrame(), facingRight ? b2body.getPosition().x - ((width / resize) / Constants.PPM) / 2 : b2body.getPosition().x + ((width / resize) / Constants.PPM) / 2 , b2body.getPosition().y - ((height / resize) / Constants.PPM) / 2, (facingRight ? width : -width) / Constants.PPM, height / Constants.PPM);
        batch.end();
    }

    public Vector2 getPosition() { return b2body.getPosition(); }

    public Body getB2body() { return b2body; }
}
