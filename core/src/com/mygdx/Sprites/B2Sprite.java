package com.mygdx.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.Tools.Constants;

public abstract class B2Sprite {
    protected Body b2body;
    protected Animation animation;
    protected float width;
    protected float height;
    protected boolean facingRight;

    public B2Sprite() {
        animation = new Animation();
        facingRight = true;
    }

    public void setAnimation(TextureRegion[] region, float delay, boolean loopLastFrame) {
        animation.setFrames(region, delay, loopLastFrame);
        width = region[0].getRegionWidth();
        height = region[0].getRegionWidth();
    }

    public void handleAnimation() { }

    public void update(float delta) {
        animation.update(delta);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(animation.getFrame(), facingRight ? b2body.getPosition().x - (width / Constants.PPM) / 2 : b2body.getPosition().x + (width / Constants.PPM) / 2 , b2body.getPosition().y - (height / Constants.PPM) / 2, (facingRight ? width : -width) / Constants.PPM, height / Constants.PPM);
        batch.end();
    }
}
